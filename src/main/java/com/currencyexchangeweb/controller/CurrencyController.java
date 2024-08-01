package com.currencyexchangeweb.controller;

import com.currencyexchangeweb.dto.CurrencyDTO;
import com.currencyexchangeweb.service.CurrencyService;
import com.currencyexchangeweb.util.JsonConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/currencies", "/currency/*"})
public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService;

    public CurrencyController() {
        currencyService = CurrencyService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String servletPath = req.getServletPath();
        try (PrintWriter writer = resp.getWriter()) {
            if ("/currencies".equals(servletPath)) {
                List<CurrencyDTO> currencies = currencyService.getAllCurrencies();
                System.out.println(currencies.size());
                writer.write(JsonConverter.convertToJson(currencies));
                resp.setStatus(200);
            } else if ("/currency".equals(servletPath) && pathInfo != null && !pathInfo.equals("/")) {
                String code = req.getPathInfo().substring(1);
                CurrencyDTO currency = currencyService.getCurrencyByCode(code);
                if (currency == null) {
                    writer.write("Валюта не найдена");
                    resp.setStatus(404);
                } else {
                    writer.write(JsonConverter.convertToJson(currency));
                    resp.setStatus(200);
                }
            } else if ("/currency".equals(servletPath) && (pathInfo == null || pathInfo.equals("/"))) {
                writer.write("Код валюты отсутствует в адресе");
                resp.setStatus(400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/currencies".equals(req.getServletPath())) {
            resp.setContentType("application/json");
            try {
                String name = req.getParameter("name");
                String code = req.getParameter("code");
                String sign = req.getParameter("sign");
                CurrencyDTO currency = currencyService.addCurrency(name, code, sign);
                resp.setStatus(201);
                System.out.println(currency);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write(JsonConverter.convertToJson(currency));
                }
            } catch (IllegalArgumentException e) {
                resp.setStatus(400);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write("Отсутствует одно или несколько полей формы");
                }
            } catch (SQLException e) {
                resp.setStatus(409);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write("Конфликт: валюта уже есть в БД");
                }
            }
        }
    }
}

