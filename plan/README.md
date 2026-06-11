# Membership Plan

Spring Boot service demonstrating a membership plan system.

Highlights
- Spring Boot 4 (Java 21)
- H2 in-memory database
- Spring Data JPA
- Clear, small REST controllers and service layer
- Strategy pattern used for tier evaluation
- OpenAPI (Swagger) UI available at `/`


Requirements:

1. Membership Plans
   - Monthly
   - Quarterly
   - Yearly

2. Membership Tiers
   - Silver
   - Gold
   - Platinum

3. Features
   - Subscribe
   - Upgrade Tier
   - Downgrade Tier
   - Cancel Subscription
   - Get Current Membership

4. Use:
   - Spring Boot
   - In memory database
   - JPA
   - Strategy Pattern for tier evaluation

5. APIs should be production ready.


Run

Windows (PowerShell):

```powershell
.\mvnw.cmd clean package
java -cp target\classes;target\dependency\* com.example.membership.plan.PlanApplication
```

Endpoints (examples)
- `GET /plans` — list plans
- `GET /tiers` — list tiers
- `POST /subscriptions` — create subscription
- `PUT /subscriptions/{userId}/upgrade` — upgrade tier
- `PUT /subscriptions/{userId}/downgrade` — downgrade tier

For example

PUT: /subscriptions/{userId}/tracking

userId: 101

for request body use
{
  "monthStartDate": "2026-06-01",
  "orderCount": 25,
  "totalSpend": 5400.75
}

POST: /subscriptions

for request body use
{
 "userId": 104,
  "planInterval": "MONTHLY",
"tierName": "GOLD"
}
