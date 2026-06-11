Membership Plan
Spring Boot service demonstrating a membership plan system.

Highlights

Spring Boot 4 (Java 21)
H2 in-memory database
Spring Data JPA
Clear, small REST controllers and service layer
Strategy pattern used for tier evaluation
OpenAPI (Swagger) UI available at /
Requirements:

Membership Plans

Monthly
Quarterly
Yearly
Membership Tiers

Silver
Gold
Platinum
Features

Subscribe
Upgrade Tier
Downgrade Tier
Cancel Subscription
Get Current Membership
Use:

Spring Boot
In memory database
JPA
Strategy Pattern for tier evaluation
APIs should be production ready.

Run

Windows (PowerShell):

.\mvnw.cmd clean package
java -cp target\classes;target\dependency\* com.example.membership.plan.PlanApplication
Endpoints (examples)

GET /plans — list plans
GET /tiers — list tiers
POST /subscriptions — create subscription
PUT /subscriptions/{userId}/upgrade — upgrade tier
PUT /subscriptions/{userId}/downgrade — downgrade tier
For example

PUT: /subscriptions/{userId}/tracking

userId: 101

for request body use { "monthStartDate": "2026-06-01", "orderCount": 25, "totalSpend": 5400.75 }

POST: /subscriptions

for request body use { "userId": 104, "planInterval": "MONTHLY", "tierName": "GOLD" }


                     +------------------+
                     | Membership API   |
                     +--------+---------+
                              |
        ------------------------------------------
        |                |            |          |
        v                v            v          v

+---------------+ +--------------+ +------------+ +-------------+
| Plan Service  | | Tier Service | | BenefitSvc | | Subscription|
+---------------+ +--------------+ +------------+ +-------------+
                                                |
                                                |
                                                v

                                    +--------------------+
                                    | Tier EvaluationSvc |
                                    +---------+----------+
                                              |
                                              v

                                    +--------------------+
                                    | Order Statistics   |
                                    +--------------------+
