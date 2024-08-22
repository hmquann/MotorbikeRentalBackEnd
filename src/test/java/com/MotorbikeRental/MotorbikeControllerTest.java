//package com.MotorbikeRental.controller;
//
//import com.MotorbikeRental.entity.Motorbike;
//import com.MotorbikeRental.entity.MotorbikeStatus;
//import com.MotorbikeRental.service.MotorbikeService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class MotorbikeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MotorbikeService motorbikeService;
//
//    private final String accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImpvaG4uZG9lQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE5NjYwODk5LCJleHAiOjE3MTk3NDcyOTl9.NE1vmXnO8AJT2Pw0Mszv3vjlVJsWyO9wDKqJ_8qdeP4";
//
//    @Test
//    public void testGetAllMotorbike() throws Exception {
//        List<Motorbike> motorbikes = Arrays.asList(new Motorbike(), new Motorbike());
//
//        when(motorbikeService.getAllMotorbike()).thenReturn(motorbikes);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/motorbike/allMotorbike")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id").exists());
//    }
//
//    @Test
//    public void testGetMotorbikeWithPagination() throws Exception {
//        int page = 0;
//        int pageSize = 10;
//        List<Motorbike> motorbikes = Arrays.asList(new Motorbike(), new Motorbike());
//
//        Page<Motorbike> motorbikePage = new PageImpl<>(motorbikes);
//
//        when(motorbikeService.getMotorbikeWithPagination(page, pageSize)).thenReturn(motorbikePage);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/motorbike/allMotorbike/{page}/{pageSize}", page, pageSize)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content", hasSize(2)))
//                .andExpect(jsonPath("$.content[0].id").exists());
//    }
//
//    @Test
//    public void testSearchByPlate() throws Exception {
//        String searchTerm = "ABC123";
//        int page = 0;
//        int size = 10;
//        List<Motorbike> foundMotorbikes = Arrays.asList(new Motorbike(), new Motorbike());
//
//        when(motorbikeService.searchByPlate(searchTerm, page, size)).thenReturn(new PageImpl<>(foundMotorbikes));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/motorbike/search")
//                        .header("Authorization", accessToken)
//                        .param("searchTerm", searchTerm)
//                        .param("page", String.valueOf(page))
//                        .param("size", String.valueOf(size))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content", hasSize(2)))
//                .andExpect(jsonPath("$.content[0].id").exists());
//    }
//
//    @Test
//    public void testToggleMotorbikeStatus() throws Exception {
//        Long motorbikeId = 1L;
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/motorbike/toggleStatus/{id}", motorbikeId)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Toggle motorbike status successfully."));
//    }
//
//    @Test
//    public void testApproveMotorbike() throws Exception {
//        Long motorbikeId = 1L;
//
//        Motorbike motorbike = new Motorbike();
//        motorbike.setId(motorbikeId);
//
//        when(motorbikeService.approveMotorbike(motorbikeId)).thenReturn(motorbike);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/motorbike/approve/{id}", motorbikeId)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(motorbikeId.intValue()));
//    }
//
//    @Test
//    public void testRejectMotorbike() throws Exception {
//        Long motorbikeId = 1L;
//
//        Motorbike motorbike = new Motorbike();
//        motorbike.setId(motorbikeId);
//
//        when(motorbikeService.rejectMotorbike(motorbikeId)).thenReturn(motorbike);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/motorbike/reject/{id}", motorbikeId)
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(motorbikeId.intValue()));
//    }
//
//    @Test
//    public void testGetAllActiveMotorbike() throws Exception {
//        List<Motorbike> motorbikes = Arrays.asList(new Motorbike(), new Motorbike());
//
//        when(motorbikeService.getAllMotorbikeByStatus(any())).thenReturn(motorbikes);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/motorbike/activeMotorbikeList")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2));
//    }
//
//    @Test
//    public void testRegisterMotorbike() throws Exception {
//        Long modelId = 1L;
//        Motorbike motorbike = new Motorbike();
//        motorbike.setId(1L);
//        motorbike.setPrice(10000L);
//        motorbike.setMotorbikePlate("ABC123");
//        motorbike.setStatus(MotorbikeStatus.ACTIVE);
//
//        when(motorbikeService.registerMotorbike(any())).thenReturn(motorbike);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.price").value(10000))
//                .andExpect(jsonPath("$.motorbikePlate").value("ABC123"))
//                .andExpect(jsonPath("$.status").value("ACTIVE"));
//    }
//
//    @Test
//    public void testRegisterMotorbike_Validation() throws Exception {
//        // Test case 1: Empty payload
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 2: Missing required fields
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 3: Invalid price (negative value)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":-10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 4: Missing motorbikePlate
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 5: Empty motorbikePlate
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 6: Invalid status
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"INVALID\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 7: Missing model id
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\"}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 8: Invalid model id (negative value)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":-1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 9: Valid payload
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.price").value(10000))
//                .andExpect(jsonPath("$.motorbikePlate").value("ABC123"))
//                .andExpect(jsonPath("$.status").value("ACTIVE"));
//
//        // Test case 10: Additional validation scenario (e.g., validate motorbikePlate format or length)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"INVALID-PLATE\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 11: Price is zero
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":0,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 12: Invalid motorbikePlate length (less than minimum)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"AB1\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 13: Invalid motorbikePlate length (exceeds maximum)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABCD123456789\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 14: Invalid motorbikePlate format (special characters)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC@123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 15: Missing price field
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 16: Unauthorized access (missing Authorization header)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isUnauthorized());
//
//        // Test case 17: Invalid Authorization token
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", "Bearer invalid_token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isUnauthorized());
//
//        // Test case 18: Model with given id does not exist
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":999}}"))
//                .andExpect(status().isBadRequest());
//
//        // Test case 19: Duplicate motorbikePlate (already exists in database)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"ACTIVE\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isConflict());
//
//        // Test case 20: Invalid status value (e.g., null or empty string)
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/motorbike/register")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":10000,\"motorbikePlate\":\"ABC123\",\"status\":\"\",\"model\":{\"id\":1}}"))
//                .andExpect(status().isBadRequest());
//    }
//
//}
