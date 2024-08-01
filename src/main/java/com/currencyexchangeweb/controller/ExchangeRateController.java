package com.currencyexchangeweb.controller;

import com.currencyexchangeweb.dto.ExchangeRateDTO;
import com.currencyexchangeweb.service.ExchangeRateService;
import com.currencyexchangeweb.util.JsonConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/exchangeRates", "/exchangeRate/*"})
public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController() {
        this.exchangeRateService = ExchangeRateService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        String servletPath = req.getServletPath();

        if ("/exchangeRates".equals(servletPath)) {
            List<ExchangeRateDTO> rates = exchangeRateService.getAllRates();
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(JsonConverter.convertToJson(rates));
                resp.setStatus(200);
            }
        } else if ("/exchangeRate".equals(servletPath) && pathInfo != null && !pathInfo.equals("/")) {
            try {
                ExchangeRateDTO rate = exchangeRateService.getRateByPair(pathInfo);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write(JsonConverter.convertToJson(rate));
                    resp.setStatus(200);
                }
            } catch (NameNotFoundException e) {
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write(e.getMessage());
                    resp.setStatus(404);
                }
            }
        } else if ("/exchangeRate".equals(servletPath) && (pathInfo == null || pathInfo.equals("/"))) {
            try (PrintWriter writer = resp.getWriter()) {
                writer.write("Код валют пары отсутствует в адресе");
                resp.setStatus(400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Добавление валюты
        if ("/exchangeRates".equals(req.getServletPath())) {
            resp.setContentType("application/json");
            try {
                String baseCurrencyCode = req.getParameter("baseCurrencyCode");
                String targetCurrencyCode = req.getParameter("targetCurrencyCode");
                if (req.getParameter("rate") == null) {
                    throw new IllegalArgumentException("Отсутствует одно или несколько полей формы");
                }
                Double rate = Double.valueOf(req.getParameter("rate"));
                ExchangeRateDTO exchangeRate = exchangeRateService.addCurrency(baseCurrencyCode, targetCurrencyCode, rate);
                resp.setStatus(201);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write(JsonConverter.convertToJson(exchangeRate));
                }
            } catch (IllegalArgumentException e) {
                resp.setStatus(400);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write("Отсутствует одно или несколько полей формы");
                }
            } catch (NameNotFoundException e) {
                resp.setStatus(404);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write(e.getMessage());
                }
            } catch (SQLException e) {
                resp.setStatus(409);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write("Конфликт: валютная пара с таким кодом уже есть в БД");
                }
            }
        }

        //Для метода PATCH
        if ("/exchangeRate".equals(req.getServletPath())) {
            resp.setContentType("application/json");
            try {
                String pair = req.getPathInfo();
                if (req.getParameter("rate") == null) {
                    throw new IllegalArgumentException("Отсутствует нужное поле формы");
                }
                ExchangeRateDTO rate = exchangeRateService.updateRatePair(pair, Double.valueOf(req.getParameter("rate")));
                resp.setStatus(200);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write(JsonConverter.convertToJson(rate));
                }
            } catch (IllegalArgumentException e) {
                resp.setStatus(400);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write("Отсутствует одно или несколько полей формы");
                }
            } catch (NameNotFoundException e) {
                System.out.println(e.getMessage());
                resp.setStatus(404);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write("Валютная пара отсутствует в базе данных");
                }
            } catch (SQLException e) {
                resp.setStatus(409);
                try (PrintWriter writer = resp.getWriter()) {
                    writer.write(e.getMessage());
                    writer.write("Конфликт: валютная пара с таким кодом уже есть в БД");
                }
            }
        }
    }
}
