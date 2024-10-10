INSERT INTO coupons (
    coupon_type, total_quantity, issued_quantity, discount_amount, min_available_amount, date_issue_start, date_issue_end, created_at, updated_at
) VALUES (
             'FIRST_COME_FIRST_SERVED', 100, 0, 5000, 10000, '2024-01-01 00:00:00', '2024-12-31 23:59:59', NOW(), NOW()
         );