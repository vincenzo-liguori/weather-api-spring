package it.vlig.weather_api_spring.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.vlig.weather_api_spring.dto.error.ErrorResponse;
import it.vlig.weather_api_spring.entity.ApiClient;
import it.vlig.weather_api_spring.repository.ApiClientRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

  private static final String API_KEY_HEADER = "X-API-KEY";

  private static final Set<String> EXCLUDED_PATHS = Set.of("/api-clients");

  private final ApiClientRepository repository;
  private final ObjectMapper objectMapper;

  public ApiKeyAuthFilter(ApiClientRepository repository, ObjectMapper objectMapper) {
    this.repository = repository;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
    throws ServletException, IOException {

    if (EXCLUDED_PATHS.contains(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    String apiKey = request.getHeader(API_KEY_HEADER);
    if (apiKey == null || apiKey.isBlank()) {
      writeUnauthorizedResponse(response, "Missing API Key");
      return;
    }

    Optional<ApiClient> clientOpt = repository.findByApiKey(apiKey);

    if (clientOpt.isEmpty()) {
      writeUnauthorizedResponse(response, "Invalid API Key");
      return;
    }

    request.setAttribute("authenticatedClient", clientOpt.get());
    filterChain.doFilter(request, response);
  }

  private void writeUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.UNAUTHORIZED.value());
    String json = objectMapper.writeValueAsString(errorResponse);
    response.getWriter().write(json );
  }
}
