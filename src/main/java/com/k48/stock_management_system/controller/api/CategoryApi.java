package com.k48.stock_management_system.controller.api;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@Tag(name = "Category", description = "API pour la gestion des category d'articles")
@RequestMapping(APP_ROOT)
public interface CategoryApi {
}
