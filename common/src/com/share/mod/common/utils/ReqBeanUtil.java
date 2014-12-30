package com.share.mod.common.utils;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.BeanUtils;

public class ReqBeanUtil {
	private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static final Validator validator = factory.getValidator();
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(HttpServletRequest request, Class<T> clazz)
	{
		T object;
		try
		{
			object = (T)Class.forName(clazz.getName()).newInstance();
			BeanUtils.populate(object,request.getParameterMap());
		}catch (Exception e){
			
			throw new InvalidParameterException("Beans trans ERROR!");
		}
		
		Set<ConstraintViolation<T>> sets = validator.validate(object);
		if (sets.size() == 0)
			return object;
		else
			throw new InvalidParameterException(sets.iterator().next().getMessage());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Map<String, String[]> map, Class<T> clazz)
	{
		T object;
		try
		{
			object = (T)Class.forName(clazz.getName()).newInstance();
			BeanUtils.populate(object,map);
		}catch (Exception e)
		{
			throw new InvalidParameterException("#MOD# Beans trans ERROR!");
		}
		
		Set<ConstraintViolation<T>> sets = validator.validate(object);
		if (sets.size() == 0)
			return object;
		else
			throw new InvalidParameterException("#MOD# " + sets.iterator().next().getMessage());
	}
}
