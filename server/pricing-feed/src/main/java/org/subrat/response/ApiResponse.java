package org.subrat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    T data;
    private String status;
    private String message;
    private Long total;
    private Integer totalPages;
    private Integer pageNum;
}
