package com.ecommerce.controller;

import com.ecommerce.dto.checkout.CheckoutRequest;
import com.ecommerce.dto.checkout.CheckoutResponse;
import com.ecommerce.dto.pedido.PedidoDetalheResponse;
import com.ecommerce.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/finalizar")
    public ResponseEntity<CheckoutResponse> finalizar(@RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(checkoutService.finalizar(request));
    }

    @GetMapping("/resumo/{pedidoCodigo}")
    public ResponseEntity<PedidoDetalheResponse> resumo(@PathVariable Integer pedidoCodigo) {
        return ResponseEntity.ok(checkoutService.detalharPedido(pedidoCodigo));
    }
}
