package hoang.shop.order.dto.response;

public record DashboardOverviewResponse(
        RevenueSummaryResponse revenue,
        MetricSummaryResponse orderCount,
        MetricSummaryResponse visitorCount,
        MetricSummaryResponse newUserCount
) {
}
