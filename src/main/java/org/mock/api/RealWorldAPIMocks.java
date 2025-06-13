package org.mock.api;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author Naveen Khunteta
 */
public class RealWorldAPIMocks {

    // *********************** E-COMMERCE EXAMPLES *****************************

    /**
     * Mock Product Catalog API with pagination
     */
    public static void getProductCatalogWithPagination() {
        stubFor(get(urlPathMatching("/api/products"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("limit", equalTo("10"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("products-page1.json")
                        .withHeader("X-Total-Count", "150")
                        .withHeader("X-Page-Count", "15")));
    }

    /**
     * Mock Product Search with filters
     */
    public static void searchProductsWithFilters() {
        stubFor(get(urlPathEqualTo("/api/products/search"))
                .withQueryParam("category", equalTo("electronics"))
                .withQueryParam("minPrice", matching("\\d+"))
                .withQueryParam("maxPrice", matching("\\d+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("filtered-products.json")));
    }

    /**
     * Mock Shopping Cart Operations
     */
    public static void addToCart() {
        stubFor(post(urlEqualTo("/api/cart/items"))
                .withHeader("Authorization", matching("Bearer .*"))
                .withRequestBody(containing("productId"))
                .withRequestBody(containing("quantity"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Item added to cart\", \"cartId\": \"cart-123\", \"totalItems\": 3}")));
    }

    /**
     * Mock Order Placement with different scenarios
     */
    public static void placeOrderSuccess() {
        stubFor(post(urlEqualTo("/api/orders"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(matchingJsonPath("$.total[?(@.amount > 0)]"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("order-success.json")));
    }

    public static void placeOrderInsufficientStock() {
        stubFor(post(urlEqualTo("/api/orders"))
                .withRequestBody(containing("productId\":\"out-of-stock-item"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Insufficient stock\", \"code\": \"STOCK_ERROR\"}")));
    }

    // *********************** PAYMENT GATEWAY EXAMPLES *****************************

    /**
     * Mock Successful Payment Processing
     */
    public static void processPaymentSuccess() {
        stubFor(post(urlEqualTo("/api/payments"))
                .withHeader("Authorization", containing("Bearer"))
                .withRequestBody(matchingJsonPath("$.amount"))
                .withRequestBody(matchingJsonPath("$.currency"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"transactionId\": \"txn_123456\", \"status\": \"SUCCESS\", \"amount\": 99.99}")));
    }

    /**
     * Mock Payment Failure Scenarios
     */
    public static void processPaymentDeclined() {
        stubFor(post(urlEqualTo("/api/payments"))
                .withRequestBody(containing("4000000000000002")) // Declined card number
                .willReturn(aResponse()
                        .withStatus(402)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Card declined\", \"code\": \"CARD_DECLINED\"}")));
    }

    public static void processPaymentTimeout() {
        stubFor(post(urlEqualTo("/api/payments"))
                .withRequestBody(containing("timeout-test"))
                .willReturn(aResponse()
                        .withStatus(408)
                        .withFixedDelay(5000)
                        .withBody("{\"error\": \"Payment timeout\", \"code\": \"TIMEOUT\"}")));
    }

    // *********************** USER MANAGEMENT EXAMPLES *****************************

    /**
     * Mock User Registration with validation
     */
    public static void registerUserSuccess() {
        stubFor(post(urlEqualTo("/api/users/register"))
                .withRequestBody(matchingJsonPath("$.email[?(@=~/.+@.+\\..+/)]")) // Email validation
                .withRequestBody(matchingJsonPath("$.password[?(@.length() >= 8)]")) // Password length
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("user-registration-success.json")));
    }

    public static void registerUserEmailExists() {
        stubFor(post(urlEqualTo("/api/users/register"))
                .withRequestBody(containing("existing@email.com"))
                .willReturn(aResponse()
                        .withStatus(409)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Email already exists\", \"code\": \"EMAIL_EXISTS\"}")));
    }

    /**
     * Mock JWT Token Authentication
     */
    public static void loginUserSuccess() {
        stubFor(post(urlEqualTo("/api/auth/login"))
                .withRequestBody(matchingJsonPath("$.email"))
                .withRequestBody(matchingJsonPath("$.password"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"expiresIn\": 3600, \"user\": {\"id\": 1, \"email\": \"user@example.com\"}}")));
    }

    public static void loginInvalidCredentials() {
        stubFor(post(urlEqualTo("/api/auth/login"))
                .withRequestBody(containing("wrong-password"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Invalid credentials\", \"code\": \"INVALID_CREDENTIALS\"}")));
    }

    // *********************** BANKING/FINTECH EXAMPLES *****************************

    /**
     * Mock Account Balance Check
     */
    public static void getAccountBalance() {
        stubFor(get(urlPathMatching("/api/accounts/\\d+/balance"))
                .withHeader("Authorization", matching("Bearer .*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"accountId\": \"ACC123\", \"balance\": 1500.75, \"currency\": \"USD\", \"lastUpdated\": \"2025-06-13T10:30:00Z\"}")));
    }

    /**
     * Mock Money Transfer with different scenarios
     */
    public static void transferMoneySuccess() {
        stubFor(post(urlEqualTo("/api/transfers"))
                .withHeader("Authorization", matching("Bearer .*"))
                .withRequestBody(matchingJsonPath("$.amount[?(@.value <= 10000)]")) // Transfer limit
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("transfer-success.json")));
    }

    public static void transferMoneyInsufficientFunds() {
        stubFor(post(urlEqualTo("/api/transfers"))
                .withRequestBody(matchingJsonPath("$.amount[?(@.value > 10000)]"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Insufficient funds\", \"code\": \"INSUFFICIENT_FUNDS\"}")));
    }

    // *********************** NOTIFICATION SERVICE EXAMPLES *****************************

    /**
     * Mock Email Notification Service
     */
    public static void sendEmailNotification() {
        stubFor(post(urlEqualTo("/api/notifications/email"))
                .withHeader("API-Key", matching("key_.*"))
                .withRequestBody(matchingJsonPath("$.to"))
                .withRequestBody(matchingJsonPath("$.subject"))
                .withRequestBody(matchingJsonPath("$.body"))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"messageId\": \"msg_123456\", \"status\": \"QUEUED\"}")));
    }

    /**
     * Mock SMS Service with rate limiting
     */
    public static void sendSMSWithRateLimit() {
        stubFor(post(urlEqualTo("/api/notifications/sms"))
                .inScenario("SMS Rate Limit")
                .whenScenarioStateIs("Started")
                .willSetStateTo("First SMS Sent")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"messageId\": \"sms_123\", \"status\": \"SENT\"}")));

        stubFor(post(urlEqualTo("/api/notifications/sms"))
                .inScenario("SMS Rate Limit")
                .whenScenarioStateIs("First SMS Sent")
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader("Retry-After", "60")
                        .withBody("{\"error\": \"Rate limit exceeded\", \"retryAfter\": 60}")));
    }

    // *********************** SOCIAL MEDIA API EXAMPLES *****************************

    /**
     * Mock Social Media Post Creation
     */
    public static void createSocialPost() {
        stubFor(post(urlEqualTo("/api/posts"))
                .withHeader("Authorization", containing("Bearer"))
                .withRequestBody(matchingJsonPath("$.content[?(@.length() <= 280)]")) // Character limit
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("post-created.json")));
    }

    /**
     * Mock Getting User Feed with pagination
     */
    public static void getUserFeed() {
        stubFor(get(urlPathEqualTo("/api/feed"))
                .withHeader("Authorization", matching("Bearer .*"))
                .withQueryParam("page", matching("\\d+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("user-feed.json")
                        .withHeader("X-Has-More", "true")));
    }

    // *********************** FILE UPLOAD/DOWNLOAD EXAMPLES *****************************

    /**
     * Mock File Upload
     */
    public static void uploadFileSuccess() {
        stubFor(post(urlEqualTo("/api/files/upload"))
                .withHeader("Content-Type", containing("multipart/form-data"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"fileId\": \"file_123\", \"url\": \"https://cdn.example.com/file_123\", \"size\": 1024576}")));
    }

    public static void uploadFileTooLarge() {
        stubFor(post(urlEqualTo("/api/files/upload"))
                .withHeader("Content-Length", matching("[1-9]\\d{7,}")) // > 10MB
                .willReturn(aResponse()
                        .withStatus(413)
                        .withBody("{\"error\": \"File too large\", \"maxSize\": \"10MB\"}")));
    }

    // *********************** WEATHER API EXAMPLES *****************************

    /**
     * Mock Weather API with location
     */
    public static void getCurrentWeather() {
        stubFor(get(urlPathEqualTo("/api/weather/current"))
                .withQueryParam("lat", matching("-?\\d+\\.\\d+"))
                .withQueryParam("lon", matching("-?\\d+\\.\\d+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("current-weather.json")));
    }

    public static void getWeatherForecast() {
        stubFor(get(urlPathMatching("/api/weather/forecast/\\d+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("weather-forecast.json")
                        .withHeader("Cache-Control", "max-age=1800")));
    }

    // *********************** RATE LIMITING & CIRCUIT BREAKER EXAMPLES *****************************

    /**
     * Mock API with Rate Limiting
     */
    public static void apiWithRateLimit() {
        // First 5 requests succeed
        stubFor(get(urlEqualTo("/api/data"))
                .inScenario("Rate Limiting")
                .whenScenarioStateIs("Started")
                .willSetStateTo("Limit Reached")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("X-Rate-Limit-Remaining", "4")
                        .withBody("{\"data\": \"success\"}")));

        // Subsequent requests are rate limited
        stubFor(get(urlEqualTo("/api/data"))
                .inScenario("Rate Limiting")
                .whenScenarioStateIs("Limit Reached")
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader("X-Rate-Limit-Reset", "3600")
                        .withBody("{\"error\": \"Rate limit exceeded\"}")));
    }

    /**
     * Mock Service Degradation
     */
    public static void serviceDegradation() {
        stubFor(get(urlEqualTo("/api/slow-service"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(2000) // 2 second delay
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"data\": \"slow response\", \"performance\": \"degraded\"}")));
    }
}
