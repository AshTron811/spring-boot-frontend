package com.example.afas.attendance_frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class FrontendController {

    @Autowired
    private RestTemplate restTemplate;

    // Update with your deployed Python backend URL.
    private final String PYTHON_BACKEND_URL = "https://python-backend-production-aa78.up.railway.app";

    // Home page endpoint
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the Attendance System Frontend.");
        return "index";  // Renders index.html
    }

    // View Attendance endpoint
    @GetMapping("/viewAttendance")
    public String viewAttendance(Model model) {
        String attendanceData;
        try {
            attendanceData = restTemplate.getForObject(PYTHON_BACKEND_URL + "/attendance", String.class);
        } catch (Exception e) {
            attendanceData = "Error retrieving attendance: " + e.getMessage();
        }
        model.addAttribute("attendanceData", attendanceData);
        return "viewAttendance";  // Renders viewAttendance.html
    }

    // Display the Capture Live Face form
    @GetMapping("/captureLiveFace")
    public String captureLiveFace(Model model) {
        return "captureLiveFace";  // Renders captureLiveFace.html
    }

    // Process the Capture Live Face form submission
    @PostMapping("/captureLiveFaceSubmit")
    public String captureLiveFaceSubmit(@RequestParam("name") String name,
                                        @RequestParam("imageData") String imageData,
                                        Model model) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", name);
        body.add("imageData", imageData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String responseMessage;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(PYTHON_BACKEND_URL + "/capture_face", requestEntity, String.class);
            responseMessage = response.getBody();
        } catch (Exception e) {
            responseMessage = "Error capturing face: " + e.getMessage();
        }
        model.addAttribute("message", responseMessage);
        return "captureLiveFace";  // Renders captureLiveFace.html with the response message
    }

    // Attendance Control page: provides both Start and Stop Attendance options
    @GetMapping("/attendanceControl")
    public String attendanceControl(Model model) {
        return "attendanceControl";  // Renders attendanceControl.html
    }

    // Start Attendance endpoint
    @PostMapping("/startAttendance")
    public String startAttendance(Model model) {
        String startMessage;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(PYTHON_BACKEND_URL + "/start_attendance", null, String.class);
            startMessage = response.getBody();
        } catch (Exception e) {
            startMessage = "Error starting attendance system: " + e.getMessage();
        }
        model.addAttribute("controlMessage", startMessage);
        return "attendanceControl";  // Renders attendanceControl.html with updated message
    }

    // Stop Attendance endpoint
    @PostMapping("/stopAttendance")
    public String stopAttendance(Model model) {
        String stopMessage;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(PYTHON_BACKEND_URL + "/stop_attendance", null, String.class);
            stopMessage = response.getBody();
        } catch (Exception e) {
            stopMessage = "Error stopping attendance system: " + e.getMessage();
        }
        model.addAttribute("controlMessage", stopMessage);
        return "attendanceControl";  // Renders attendanceControl.html with updated message
    }

    // System Status endpoint: retrieves status from the Python backend
    @GetMapping("/status")
    public String status(Model model) {
        String status;
        try {
            status = restTemplate.getForObject(PYTHON_BACKEND_URL + "/", String.class);
        } catch (Exception e) {
            status = "Error connecting to backend: " + e.getMessage();
        }
        model.addAttribute("status", status);
        return "status";  // Renders status.html
    }

    // Known Faces endpoint: fetches a list of filenames stored in the backend's known_faces folder
    @GetMapping("/knownFaces")
    public String knownFaces(Model model) {
        try {
            ResponseEntity<String[]> response = restTemplate.getForEntity(PYTHON_BACKEND_URL + "/known_faces", String[].class);
            String[] knownFacesArray = response.getBody();
            model.addAttribute("knownFaces", knownFacesArray);
        } catch (Exception e) {
            model.addAttribute("knownFaces", new String[]{});
            model.addAttribute("message", "Error retrieving known faces: " + e.getMessage());
        }
        return "knownFaces";  // Renders knownFaces.html
    }
}
