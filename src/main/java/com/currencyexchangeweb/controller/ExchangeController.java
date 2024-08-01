package com.currencyexchangeweb.controller;

import com.currencyexchangeweb.dto.ExchangeDTO;
import com.currencyexchangeweb.service.ExchangeService;
import com.currencyexchangeweb.util.JsonConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeController extends HttpServlet {
    private final ExchangeService exchangeService;

    public ExchangeController() {
        this.exchangeService = ExchangeService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String from = req.getParameter("from");
        String to = req.getParameter("to");
        Double amount = Double.valueOf(req.getParameter("amount"));

        try (PrintWriter writer = resp.getWriter()) {
            try {
                ExchangeDTO dto = exchangeService.doExchange(from, to, amount);
                System.out.println(JsonConverter.convertToJson(dto));
                resp.setStatus(200);
                writer.write(JsonConverter.convertToJson(dto));
            } catch (NullPointerException e) {
                resp.setStatus(400);
                writer.write("{\"message\": \"Одна (или обе) валюта не найдена\"}");
            } catch (SQLException e) {
                resp.setStatus(500);
                writer.write("{\"message\":\"Internal server error\"}");
            } catch (Exception e) {
                resp.setStatus(500);
                writer.write("{\"message\":\"Другая ошибка\"}");
            }
        }
    }
}
