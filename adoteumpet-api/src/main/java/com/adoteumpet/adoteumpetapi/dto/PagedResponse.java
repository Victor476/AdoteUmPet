package com.adoteumpet.adoteumpetapi.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO para resposta paginada da API.
 * 
 * @param <T> Tipo dos dados da página
 */
public class PagedResponse<T> {
    private List<T> data;
    private int page;
    private int size;
    private long total;
    private int totalPages;
    
    public PagedResponse() {}
    
    public PagedResponse(List<T> data, int page, int size, long total, int totalPages) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = totalPages;
    }
    
    /**
     * Converte um objeto Page do Spring Data para PagedResponse
     */
    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
    
    // Getters and Setters
    public List<T> getData() {
        return data;
    }
    
    public void setData(List<T> data) {
        this.data = data;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}