package ru.poputi.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.poputi.CommonUtils;
import ru.poputi.dao.CitiesDAO;
import ru.poputi.dao.RoutesDAO;
import ru.poputi.enums.RouteModelType;
import ru.poputi.jpa.CityEntity;
import ru.poputi.jpa.RouteEntity;
import freemarker.template.TemplateException;

@Controller
public class ComboController {

	private Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private CitiesDAO citiesDao;

	@Autowired
	private RoutesDAO routesDao;

	private static final String _OPTION_PATTERN = "<option value=\"{0}\">{1}</option>";

	@RequestMapping(value = "/cities.list.json", method = RequestMethod.GET)
	public String getCitiesList(HttpServletResponse response) throws IOException {
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("text/html");
		out.write("<option selected'></option>".getBytes());
		for (CityEntity entity : citiesDao.findAll()) {
			out.write(MessageFormat.format(_OPTION_PATTERN, entity.getUniqueId(), entity.getName()).getBytes("UTF-8"));
		}
		out.flush();
		out.close();
		return null;
	}

	@RequestMapping(value = "/create.route.json", method = RequestMethod.POST)
	public String createRoute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!isLoggedIn(request)) {
			log.error("Tried to create route without cookies");
		} else {
			Map<String, String[]> map = request.getParameterMap();
			String regFrom = map.get("regComboFrom")[0];
			String throughCities = map.get("throughCities")[0];
			String regTo = map.get("regComboTo")[0];
			String regDate = map.get("regDatepicker")[0];
			String regTime = map.get("regTimepicker")[0];
			String regMesta = map.get("regMesta")[0];
			String regDopInfo = map.get("regDopInfo")[0];
			String regDriverName = map.get("regDriverName")[0];
			String regEmail = map.get("regEmail")[0];
			boolean regCheckboxEmail = Boolean.valueOf(map.get("regCheckboxEmail")[0]);
			String regPhone = map.get("regPhone")[0];
			boolean regCheckboxPhone = Boolean.valueOf(map.get("regCheckboxPhone")[0]);
			String regPrice = map.get("regPrice")[0];
			RouteEntity entity = new RouteEntity();
			entity.setRegFrom(regFrom);
			String creatorId = getCreatorId(request);
			entity.setCreatorId(creatorId);
			entity.setCreationDate(new Date());
			entity.setThroughCities(throughCities);
			entity.setRegTo(regTo);
			try {
				entity.setRegDate(CommonUtils.DATE_FORMATTER_SHORT.parse(regDate));
			} catch (ParseException e) {
				log.error("date parse ex", e);
			}
			entity.setRegTime(regTime);
			entity.setRegMesta(regMesta);
			entity.setRegDopInfo(regDopInfo);
			entity.setRegDriverName(regDriverName);
			entity.setRegEmail(regEmail);
			entity.setRegCheckboxEmail(regCheckboxEmail);
			entity.setRegPhone(regPhone);
			entity.setRegCheckboxPhone(regCheckboxPhone);
			entity.setRegPrice(regPrice);
			boolean isSuccess = routesDao.createRoute(entity);
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			out.write(String.valueOf(isSuccess).getBytes());
			out.flush();
			out.close();
			log.info("combocontroller :: route created, " + CommonUtils.getCityNameById(regFrom) + " - "
					+ CommonUtils.getCityNameById(regTo) + " " + regDate + " " + creatorId);
		}
		return null;
	}

	private Map<String, String> getRouteEntityAsMap(RouteEntity e, RouteModelType routeModel) {
		Map<String, String> map = new HashMap<String, String>();
		String creationDate = e.getCreationDate() == null ? "" : CommonUtils.DATE_FORMATTER.format(e.getCreationDate());
		String departureDate = e.getRegDate() == null ? "" : (CommonUtils.DATE_FORMATTER_SHORT.format(e.getRegDate()) + " " + e
				.getRegTime());
		map.put("routeId", String.valueOf(e.getId()));
		map.put("regTo", CommonUtils.getCityNameById(e.getRegTo()));
		map.put("regFrom", CommonUtils.getCityNameById(e.getRegFrom()));
		map.put("regDate", departureDate);
		map.put("regPrice", e.getRegPrice());
		map.put("isThrough", String.valueOf(e.getThroughCities() != null && !e.getThroughCities().isEmpty()));
		if (routeModel == RouteModelType.FULL || routeModel == RouteModelType.DRIVER) {
			map.put("regEmail", e.getRegEmail());
			map.put("regPhone", e.getRegPhone());
			map.put("creationDate", creationDate);
		}
		if (routeModel == RouteModelType.FULL || routeModel == RouteModelType.FOUND) {
			map.put("throughCities", CommonUtils.getThroughCitiesByIds(e.getThroughCities()));
			map.put("regMesta", e.getRegMesta());
		}
		if (routeModel == RouteModelType.FULL) {
			map.put("regDopInfo", e.getRegDopInfo());
			map.put("regDriverName", e.getRegDriverName());
		}
		return map;
	}

	private boolean isLoggedIn(HttpServletRequest request) {
		return (getCreatorId(request) != null && !getCreatorId(request).isEmpty());
	}

	private String getCreatorId(HttpServletRequest request) {
		String creatorId = null;
		if (request.getCookies() == null) {
			return null;
		}
		for (Cookie cookie : Arrays.asList(request.getCookies())) {
			if (cookie.getName() != null && cookie.getName().equals("poputiUserIdetity")) {
				creatorId = cookie.getValue();
			}
		}
		return creatorId;
	}

	@RequestMapping(value = "/get.route.list.json", method = RequestMethod.GET)
	public String getRoutesList(@RequestParam("tableType") String tableType, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		RouteModelType routeModel = RouteModelType.valueOf(tableType);
		String cityFrom = request.getParameter("cityFrom");
		String cityTo = request.getParameter("cityTo");
		Date routeDate = null;
		if (routeModel == RouteModelType.FOUND) {
			String sDate = request.getParameter("routeDate");
			if (sDate.isEmpty()) {
				log.error("getRoutesListByCreator :: date is empty");
				return null;
			}
			try {
				routeDate = CommonUtils.DATE_FORMATTER_SHORT.parse(sDate);
			} catch (ParseException e2) {
				log.error("getRoutesListByCreator :: Date ParseException, sDate =" + sDate + ", cityFrom =" + cityFrom + ", cityTo =" + cityTo, e2);
				return null;
			}
			if (cityFrom.isEmpty()) {
				log.error("getRoutesListByCreator :: cityFrom is empty");
				return null;
			} else if (cityFrom.length() > 3) {
				log.error("getRoutesListByCreator :: cityFrom.length() > 3");
				return null;
			}
			if (cityTo.isEmpty()) {
				log.error("getRoutesListByCreator :: cityTo is empty");
				return null;
			} else if (cityTo.length() > 3) {
				log.error("getRoutesListByCreator :: cityTo.length() > 3");
				return null;
			}
		}
		boolean is3days = "true".equals(request.getParameter("threeDays"));
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		StringWriter writer = new StringWriter();
		if ((routeModel == RouteModelType.DRIVER) && !isLoggedIn(request)) {
			// log.error("Tried to get drivers table without cookies");
		} else {
			List<RouteEntity> list = routeModel == RouteModelType.DRIVER ? routesDao.getRoutesByCreatorId(getCreatorId(request))
					: routeModel == RouteModelType.RECENT ? routesDao.getRoutesRecent() : routeModel == RouteModelType.FOUND ? routesDao
							.getRoutesByCityIds(cityFrom, cityTo, routeDate, is3days) : null;
			for (RouteEntity e : list) {
				try {
					CommonUtils.getFreemarkerTemplate(routeModel.getTemplateId()).process(getRouteEntityAsMap(e, routeModel), writer);
				} catch (TemplateException e1) {
					log.error("getRoutesListByCreator :: TemplateException", e1);
				}
			}
			if (routeModel != RouteModelType.RECENT) {
				String c = "";
				if (cityFrom != null && cityTo != null) {
					c = CommonUtils.getCityNameById(cityFrom) + " - " + CommonUtils.getCityNameById(cityTo);
				}
				log.info("combocontroller :: route list, " + c + ", " + routeModel);
			}
		}
		out.write(writer.toString().getBytes());
		out.flush();
		out.close();
		return null;
	}

	@RequestMapping(value = "/get.route.detailshtml.json", method = RequestMethod.GET)
	public String getRouteDetailsHtml(@RequestParam("routeId") String routeId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("text/html");
		if (!isLoggedIn(request)) {
			out.write("not_logged_in".getBytes());
			log.error("getRouteDetailsHtml :: tried to get route info without login");
		} else {
			StringWriter writer = new StringWriter();
			Map<String, String> map = getRouteEntityAsMap(routesDao.getRouteById(Long.valueOf(routeId)), RouteModelType.FULL);
			try {
				CommonUtils.getFreemarkerTemplate("detailedRoute.ftl").process(map, writer);
				log.info("getRouteDetailsHtml :: user = " + getCreatorId(request));
			} catch (TemplateException e) {
				log.error("getRouteDetailsHtml :: TemplateException", e);
			}
			out.write(writer.toString().getBytes());
		}
		out.flush();
		out.close();
		return null;
	}

	@RequestMapping(value = "/post.route.deactivate.json", method = RequestMethod.POST)
	public void deactivateRoute(@RequestParam("routeId") String routeId, HttpServletRequest request, HttpServletResponse response) {
		if (!isLoggedIn(request)) {
			log.error("Tried to deactivate route without cookies");
		} else {
			routesDao.deactivateRoute(Long.valueOf(routeId));
		}
	}

	@RequestMapping(value = "/post.contactus.json", method = RequestMethod.POST)
	public void contactUs(@RequestParam("userName") String userName, @RequestParam("userEmail") String userEmail,
			@RequestParam("userMsg") String userMsg, HttpServletRequest request, HttpServletResponse response) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("stanlymainly", "qazwsxedc_");
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("stanlymainly@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("verve111@mail.ru"));
			message.setSubject("Poputi: user's message");
			message.setText(userName + "\n" + userEmail + "\n" + userMsg);
			Transport.send(message);
		} catch (MessagingException e) {
			log.error("Mail send exception", e);
			e.getStackTrace();
		}
	}
	// !always add response to post, otherwise - not found
}
