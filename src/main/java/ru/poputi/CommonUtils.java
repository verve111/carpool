package ru.poputi;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;


public class CommonUtils {

	private static DBCreator dbCreatorBean;
	private static Logger log = LoggerFactory.getLogger(CommonUtils.class.getSimpleName());
	public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	public final static SimpleDateFormat DATE_FORMATTER_SHORT = new SimpleDateFormat("dd.MM.yyyy");
    private static Configuration cfg;
	
	public static synchronized String getCityNameById(String id) {
		String res = "";
		if (dbCreatorBean == null) {
			dbCreatorBean = (DBCreator)AppContext.getApplicationContext().getBean("DBCreatorBean");
		}
		Object o = dbCreatorBean.getCitiesMap().get(id);
		if (o != null && o instanceof String) {
			res = (String) o;
		}
		return res;
	}
	
	public static synchronized String getThroughCitiesByIds(String idsCommaSeparated) {
		String res = "";
		for (String cityId : idsCommaSeparated.split(",")) {
			res += getCityNameById(cityId.trim()) + ", ";
		}
		return res.isEmpty() ? "" : res.substring(0, res.length() - 2); 
	}
	
	public static synchronized Template getFreemarkerTemplate(String templateName) {
		if (cfg == null) {
			cfg = new Configuration();
			cfg.setClassForTemplateLoading(CommonUtils.class, "../../templates");
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
			cfg.setIncompatibleImprovements(new Version(2, 3, 20));
		}
		if (templateName == null) {
			return null;
		}
		Template t = null;
		try {
			t = cfg.getTemplate(templateName, "UTF-8");
		} catch (IOException e) {
			log.error("CommonUtils :: getFreemarkerTemplate(), template get exception", e);
		}
		return t;
	}
	
}
