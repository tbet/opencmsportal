package de.eonas.opencms.install;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class FakeSession implements HttpSession {

	public Object getAttribute(String s) {
		
		return null;
	}

	public Enumeration getAttributeNames() {
		
		return null;
	}

	public long getCreationTime() {
		
		return 0;
	}

	public String getId() {
		
		return null;
	}

	public long getLastAccessedTime() {
		
		return 0;
	}

	public int getMaxInactiveInterval() {
		
		return 0;
	}

	public ServletContext getServletContext() {
		
		return null;
	}

	public HttpSessionContext getSessionContext() {
		
		return null;
	}

	public Object getValue(String s) {
		
		return null;
	}

	public String[] getValueNames() {
		
		return null;
	}

	public void invalidate() {
		

	}

	public boolean isNew() {
		
		return false;
	}

	public void putValue(String s, Object obj) {
		

	}

	public void removeAttribute(String s) {
		

	}

	public void removeValue(String s) {
		

	}

	public void setAttribute(String s, Object obj) {

	}

	public void setMaxInactiveInterval(int i) {
		

	}

}
