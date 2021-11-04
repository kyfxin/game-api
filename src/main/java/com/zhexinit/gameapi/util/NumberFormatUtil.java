package com.zhexinit.gameapi.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 数字格式化工具类
 * @author ThinkPad
 *
 */
public class NumberFormatUtil {
	
	public static void main(String[] args) {

		String formatResult = formatMoney(123);
		System.out.println("formatResult=" + formatResult);
	}
	
	/**
	 * 将int类型数值转化为￥xxx.xx的格式，保留2位小数
	 * @param sourceValue
	 * @return
	 */
	public static String formatMoney(Integer sourceValue) {
		if (null == sourceValue) {
			return "";
		}
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
		DecimalFormat currencyFormat;
		String formatResult = "";
		try {
			currencyFormat = (DecimalFormat)numberFormat;
			formatResult = currencyFormat.format(sourceValue / 100.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formatResult;
	}
	
	public static String formatBigDecimal(BigDecimal bd) {
		if (null == bd) {
			return "0.00";
		}
		
		DecimalFormat decimalFormat = new DecimalFormat("0.##");
		return decimalFormat.format(bd.doubleValue());
	}
}
