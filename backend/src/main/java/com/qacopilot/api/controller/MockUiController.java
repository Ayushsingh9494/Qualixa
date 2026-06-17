package com.qacopilot.api.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MockUiController {

    private String loginPageHtml(String error) {
        String errorHtml = "";
        if ("invalid-email".equals(error)) {
            errorHtml = "<div id=\"email-error\" style=\"color: red; margin-bottom: 15px; font-weight: bold;\">Please enter a valid email address</div>";
        } else if ("unregistered".equals(error)) {
            errorHtml = "<div id=\"email-error\" style=\"color: red; margin-bottom: 15px; font-weight: bold;\">User not found</div>";
        }

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Login</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 50px; background-color: #f5f5f5; }
                    .container { max-width: 400px; margin: auto; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    h2 { margin-bottom: 20px; }
                    label { display: block; margin-bottom: 5px; font-weight: bold; }
                    input { width: 100%; padding: 10px; margin-bottom: 15px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
                    button { width: 100%; padding: 10px; background-color: #28a745; border: none; color: white; font-weight: bold; border-radius: 4px; cursor: pointer; }
                    button:hover { background-color: #218838; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Login</h2>
                    %s
                    <form action="/login" method="POST">
                        <label for="email">Email Address</label>
                        <input type="text" id="email" name="email" required placeholder="Enter email">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required placeholder="Enter password">
                        <button type="submit" id="loginButton">Login</button>
                    </form>
                </div>
            </body>
            </html>
            """, errorHtml);
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> loginPage(@RequestParam(required = false) String error) {
        return ResponseEntity.ok(loginPageHtml(error));
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> loginSubmit(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password
    ) {
        if (email == null || !email.contains("@")) {
            // Return page directly (URL remains /login, which is what the test case asserts)
            return ResponseEntity.ok(loginPageHtml("invalid-email"));
        } else if (!"test@example.com".equals(email)) {
            // Return page directly (URL remains /login)
            return ResponseEntity.ok(loginPageHtml("unregistered"));
        } else {
            // Redirect to dashboard
            return ResponseEntity.status(302).header("Location", "/dashboard").build();
        }
    }

    @GetMapping(value = "/dashboard", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> dashboardPage() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Dashboard</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 50px; background-color: #f5f5f5; }
                    .container { max-width: 600px; margin: auto; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    h1 { color: #007bff; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1 id="welcomeMessage">Welcome to Dashboard</h1>
                    <p>You have successfully logged in.</p>
                </div>
            </body>
            </html>
            """;
        return ResponseEntity.ok(html);
    }

    @GetMapping(value = "/forgot-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> forgotPasswordPage() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Forgot Password</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 50px; background-color: #f5f5f5; }
                    .container { max-width: 400px; margin: auto; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    h2 { margin-bottom: 20px; }
                    label { display: block; margin-bottom: 5px; font-weight: bold; }
                    input { width: 100%; padding: 10px; margin-bottom: 15px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
                    button { width: 100%; padding: 10px; background-color: #007bff; border: none; color: white; font-weight: bold; border-radius: 4px; cursor: pointer; }
                    button:hover { background-color: #0056b3; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Reset Password</h2>
                    <form action="/forgot-password" method="POST">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" required placeholder="Enter your email">
                        <button type="submit" id="submitBtn">Reset Password</button>
                    </form>
                </div>
            </body>
            </html>
            """;
        return ResponseEntity.ok(html);
    }

    @PostMapping(value = "/forgot-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> forgotPasswordSubmit(@RequestParam(required = false) String email) {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Forgot Password - Sent</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 50px; background-color: #f5f5f5; }
                    .container { max-width: 400px; margin: auto; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    .success-message { color: green; font-size: 16px; font-weight: bold; line-height: 1.5; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div id="successMessage" class="success-message">
                        If an account with that email exists, a password reset link has been sent to your email.
                    </div>
                </div>
            </body>
            </html>
            """;
        return ResponseEntity.ok(html);
    }

    @GetMapping(value = "/reset-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> resetPasswordPage() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Reset Password</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 50px; background-color: #f5f5f5; }
                    .container { max-width: 400px; margin: auto; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    h2 { margin-bottom: 20px; }
                    label { display: block; margin-bottom: 5px; font-weight: bold; }
                    input { width: 100%; padding: 10px; margin-bottom: 15px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
                    button { width: 100%; padding: 10px; background-color: #007bff; border: none; color: white; font-weight: bold; border-radius: 4px; cursor: pointer; }
                    button:hover { background-color: #0056b3; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Enter New Password</h2>
                    <form action="/reset-password" method="POST">
                        <label for="password">New Password</label>
                        <input type="password" id="password" name="password" required placeholder="Enter new password">
                        <label for="confirmPassword">Confirm Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="Confirm new password">
                        <button type="submit" id="submitBtn">Submit</button>
                    </form>
                </div>
            </body>
            </html>
            """;
        return ResponseEntity.ok(html);
    }

    @PostMapping(value = "/reset-password", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> resetPasswordSubmit() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Password Reset Successful</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 50px; background-color: #f5f5f5; }
                    .container { max-width: 400px; margin: auto; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    .success-message { color: green; font-size: 16px; font-weight: bold; line-height: 1.5; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div id="successMessage" class="success-message">
                        Your password has been successfully reset.
                    </div>
                </div>
            </body>
            </html>
            """;
        return ResponseEntity.ok(html);
    }
}
