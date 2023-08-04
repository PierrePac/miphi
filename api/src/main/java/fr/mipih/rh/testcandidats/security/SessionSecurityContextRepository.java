package fr.mipih.rh.testcandidats.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionSecurityContextRepository implements SecurityContextRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionSecurityContextRepository.class);

	
	 @Override
	    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
	        return new DeferredSecurityContext() {
	            private SecurityContext context;

	            @Override
	            public SecurityContext get() {
	                if (context == null) {
	                    HttpSession session = request.getSession(false);
	                    context = session != null ? (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) : null;

	                    if (context == null) {
	                        context = SecurityContextHolder.createEmptyContext();
	                        saveContext(context, request, null);
	                    }
	                }
	                logger.debug("Session ID : {}", request.getSession().getId());
	                return context;
	            }

	            @Override
	            public boolean isGenerated() {
	                return context != null;
	            }
	        };
	    }

	    @Override
	    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
	        HttpServletRequest request = requestResponseHolder.getRequest();
	        return loadDeferredContext(request).get();
	    }

	    @Override
	    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
	        if (context != null && context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
	        	
	            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
	        }
	    }

	    @Override
	    public boolean containsContext(HttpServletRequest request) {
	        return request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) != null;
	    }
	}
