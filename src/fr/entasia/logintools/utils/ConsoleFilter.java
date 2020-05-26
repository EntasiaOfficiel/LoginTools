package fr.entasia.logintools.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class ConsoleFilter extends AbstractFilter {

	static final String[] logincmds = new String[]{"/login", "/log", "/lo", "/l", "/register", "/reg"};


	@Override
	public Result filter(LogEvent event) {
		if (event == null) return Result.NEUTRAL;
		return validateMessage(event.getMessage());
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		return validateMessage(msg);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		return validateMessage(msg.toLowerCase());
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		if (msg == null) return Result.NEUTRAL;
		return validateMessage(msg.toString().toLowerCase());
	}
	public static Result validateMessage(Message msg) {
		if (msg == null)return Result.NEUTRAL;
		return validateMessage(msg.getFormattedMessage().toLowerCase());
	}

	public static Result validateMessage(String a) {
		if(a==null)return Result.NEUTRAL;
		if(a.contains("issued server command:")){
			for(String i : logincmds){
				if(a.contains(i))return Result.DENY;
			}
		}
		return Result.NEUTRAL;
	}

}