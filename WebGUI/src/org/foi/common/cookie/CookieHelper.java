package org.foi.common.cookie;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {

    private HttpServletRequest request;
    private HttpServletResponse response;
    public static boolean testMode = false;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public CookieHelper(){
        if(!testMode) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        }
    }

    public void setCookie(String name, String value, int expiry) {
        Cookie cookie = null;

        Cookie[] userCookies = request.getCookies();
        if (userCookies != null && userCookies.length > 0 ) {
            for (int i = 0; i < userCookies.length; i++) {
                if (userCookies[i].getName().equals(name)) {
                    cookie = userCookies[i];
                    break;
                }
            }
        }

        if (cookie != null) {
            cookie.setValue(value);
        } else {
            cookie = new Cookie(name, value);
            cookie.setPath(request.getContextPath());
        }

        cookie.setMaxAge(expiry);


        response.addCookie(cookie);
    }

    public Cookie getCookie(String name) {
        Cookie cookie = null;

        if(request==null)
            return null;

        Cookie[] userCookies = request.getCookies();
        if (userCookies != null && userCookies.length > 0 ) {
            for (int i = 0; i < userCookies.length; i++) {
                if (userCookies[i].getName().equals(name)) {
                    cookie = userCookies[i];
                    return cookie;
                }
            }
        }
        return null;
    }
}