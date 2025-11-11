# 📞 GigTasker Service Registry

This service is the **"Host"** or **"Phone Book"** for the entire GigTasker microservice platform. It is a [Spring Cloud Netflix Eureka Server](https://spring.io/projects/spring-cloud-netflix-eureka-server).

Its *only* job is to provide a central registry where all other microservices (like the `api-gateway`, `user-service`, etc.) can register themselves and discover the locations (IP and port) of other services.

---

## ✨ Core Responsibility

This service is the heart of our **service discovery** mechanism.

1.  **Registration:** When a service like `user-service` starts up, it calls the `service-registry` and says, "Hi, I'm `user-service`, and I'm running at `localhost:51234`."
2.  **Discovery:** When the `api-gateway` needs to route a request, it asks the `service-registry`, "Hey, where can I find the `user-service`?"
3.  **Health:** The registry receives a "heartbeat" from every registered service. If a service stops sending heartbeats, the registry removes it from the "phone book," ensuring traffic is not sent to a dead service.

This service is what allows our other services to run on *random ports* (`server.port: 0`) and still be found.

---

## 🛠️ Tech Stack

* **Spring Boot 3**
* **Java 25**
* **Spring Cloud Netflix Eureka Server:** The core dependency that turns this app into a registry.
* **Spring Cloud Config Client:** This service gets its *own* configuration from the `config-server`.

---

## ⚙️ Configuration

This service is configured in the `gigtasker-config` repository. On startup, it reads its `bootstrap.yml` to find the `config-server`, which then provides it with `service-registry.yml`.

The most critical piece of its configuration is to **tell it not to register with itself**:

```yaml
# in gigtasker-config/service-registry.yml
server:
  port: 8761 # The official, static port for the registry dashboard

eureka:
  client:
    # --- THIS IS THE "I AM THE BOSS" SETTING ---
    # We tell this server: "You are the registry. Do not
    # try to register yourself, and do not try to fetch
    # a registry from anyone else."
    register-with-eureka: false
    fetch-registry: false
```

---

## 🚀 How to Run

This service is **Pillar 2** of the backend infrastructure. It has a strict startup requirement.

1.  **Start Dependencies (CRITICAL):**
    * Run `docker-compose up -d` (for the "FOSS Cloud").
    * You **MUST** start the `config-server` (Pillar 1) **FIRST**.

2.  **Run this Service:**
    Once the `config-server` is running, you can start this service.
    ```bash
    # From your IDE, run ServiceRegistryApplication.java
    # Or, from the command line:
    java -jar target/service-registry-0.0.1.jar
    ```

### ✅ Verification

You can verify that this service is running by opening the **Eureka Dashboard** in your browser:

**`http://localhost:8761`**



When you start other services (like the `api-gateway` or `user-service`), they will appear in the "Instances currently registered with Eureka" list on this dashboard.