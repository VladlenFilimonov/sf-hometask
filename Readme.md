**Not implemented:**

* Docker compose
* Exceptions resolving in verification core, notification, template
* Mobile notification service, integration with Gotify
* Template aggregator should be refactored
* A lot of unit and integration tests
* Validation of messages received from message broker
* Security
* Health check
* Liveness/Readiness probes
* Metrics
* Tracing
* Failovers for DB and Redis
* Circuit breakers for Template aggregate
* Logging

Installation guide //TODO

## Modules description

### Verification public (runnable app)

* Responsible for: Http requests/response routing, validation, exceptions mapping.
* Logical flow: Receive http request -> validate -> send Verification Create/Confirm message to broker -> respond with reply message from
  message broker

### Verification core (runnable app)

* Responsible for: Applying business rules, persistence of verifications, verification confirmation/creation logic, emit Verification
  created/confirmed events.
* Logical create flow: Receive create message from broker -> apply rules -> persist -> emit Verification Created event
* Logical confirm flow: Receive confirm message from broker -> apply rules -> check codes -> persist -> emit Verification Confirmed event

### Notification (runnable app)

* Responsible for: Routing message to the corresponding integration service (email or mobile services), persistence of notifications,
  integration with template render service
* Logical flow: Receive Verification Confirmed message -> provide template from Template service -> persist notification -> emit
  Notification Created event -> send message to corresponding service (email or mobile) -> emit Notification Dispatched event

### Template (runnable app)

* Responsible for: Render corresponding templates by received data from http request
* Logical flow: Receive http request -> receive corresponding template from database(not implemented yet) -> render template -> respond with
  rendered template

### Event(SDK)

* Responsible for: Configuration of message broker, topics creation, providing convenient api for one way sending or sendAndReceive methods
* Logical one way sending flow: serialize message -> send to broker
* Logical sendAndReceive flow: serialize message -> send to broker -> wait form reply -> deserialize message

### API(SDK)

* Set of common pojo classes - contracts between other modules
