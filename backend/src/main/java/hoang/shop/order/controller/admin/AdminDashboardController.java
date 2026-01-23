package hoang.shop.order.controller.admin;

import hoang.shop.common.enums.DashboardRange;
import hoang.shop.identity.service.CurrentUserService;
import hoang.shop.order.dto.response.DashboardOverviewResponse;
import hoang.shop.order.service.DashboardService;
import hoang.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/dashboard/overview")
    public DashboardOverviewResponse getOverview(
            @RequestParam DashboardRange range
    ) {
        return dashboardService.getOverview(range);
    }
}
