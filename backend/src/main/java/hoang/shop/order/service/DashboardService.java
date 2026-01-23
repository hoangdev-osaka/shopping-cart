package hoang.shop.order.service;

import hoang.shop.common.enums.DashboardRange;
import hoang.shop.order.dto.response.DashboardOverviewResponse;
import hoang.shop.order.dto.response.MetricSummaryResponse;

public interface DashboardService {
     DashboardOverviewResponse getOverview(DashboardRange dashboardRange);
}
