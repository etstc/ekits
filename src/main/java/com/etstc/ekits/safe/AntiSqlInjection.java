package com.etstc.ekits.safe;

import java.util.regex.Pattern;

public interface AntiSqlInjection {

	static boolean hasIllegalCharacter(CharSequence input) {
		if (input == null || input.length() == 0) {
			return false;
		}
		final String regex = "(?i)('|%|--|(\\s|\\)|;|')(and|or|not|use|insert|delete|update|select|count|group|union|create|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|source|sql)(\\s|\\()|')";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(input).find();
	}
}
