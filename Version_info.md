# Version 1

This repository contains an initial, minimal Key-Value store service implemented in Java with Spring Boot. This document summarizes what exists in the project for Version 1: features, code layout, API endpoints, data models, runtime instructions, known limitations, and suggested next steps.

## Summary
- Purpose: a lightweight in-memory key-value store with optional expiry (TTL) support for learning and small demo use.
- Stack: Java 11+ (or compatible), Spring Boot, ConcurrentHashMap as the in-memory store.

## Features (implemented)
- In-memory storage using `ConcurrentHashMap` (no external database).
- Add new keys with optional expiry (expiry provided in minutes).
- Read/get keys with TTL enforcement (expired keys are removed on access).
- Check key existence.
- Delete keys.

## Public API (HTTP endpoints)
All endpoints are prefixed with `/keyvalue`.

- `GET /keyvalue/connection` — Health/connection check. Returns a welcome string.
- `POST /keyvalue` — Add a new key-value entry. Request body: JSON matching `KVStoreRequest`.
	- Example body: `{ "key": "name", "value": "alice", "expiry": 10 }` (expiry in minutes; optional)
	- Behavior: uses `putIfAbsent` so existing keys are NOT overwritten by this call.
- `GET /keyvalue/{key}` — Retrieve the entry by key.
	- Returns `404` if key not found or expired.
	- Returns the `KVEntry` (includes `key`, `value`, and internal TTL timestamp when present).
- `GET /keyvalue/{key}/exists` — Returns `true` or `false` (string) indicating if the key is present.
- `DELETE /keyvalue/{key}` — Deletes the key. Returns `404` if key not found.

## Data Models
- `KVStoreRequest` (request DTO)
	- `key` (String) — required
	- `value` (String) — required
	- `expiry` (Long) — optional; interpreted as minutes

- `KVEntry` (internal store entry)
	- `key` (String)
	- `value` (String)
	- `ttl` (Long) — epoch milliseconds when entry expires, or `null` for no expiry

## Service/Implementation Notes
- `KVStore` service stores entries inside a `ConcurrentHashMap<String, KVEntry>`.
- `add()` creates a `KVEntry` and stores it using `putIfAbsent` (so duplicates are ignored).
- `get()` checks TTL on access: if expired, the key is removed and treated as not found.
- `exists()` and `delete()` are thin wrappers around the map API.

## How to run (development)
1. Build and run with Maven from repository root:

```bash
mvn spring-boot:run
```

2. Example curl requests:

Add a key:

```bash
curl -X POST http://localhost:8080/keyvalue \
	-H "Content-Type: application/json" \
	-d '{"key":"foo","value":"bar","expiry":5}'
```

Get a key:

```bash
curl http://localhost:8080/keyvalue/foo
```

Check existence:

```bash
curl http://localhost:8080/keyvalue/foo/exists
```

Delete a key:

```bash
curl -X DELETE http://localhost:8080/keyvalue/foo
```

## Tests and project structure
- Main application: `src/main/java/com/junnu/redis/RedisApplication.java`
- Controller: `src/main/java/com/junnu/redis/KVStore/controllers/Home.java`
- Service: `src/main/java/com/junnu/redis/KVStore/KVServices/KVStore.java`
- DTO: `src/main/java/com/junnu/redis/KVStore/dto/KVStoreRequest.java`
- Internal model: `src/main/java/com/junnu/redis/KVStore/KVServices/KVEntry.java`
- Tests (if present): `src/test/java/...`

## Known limitations (Version 1)
- No persistence: all data is stored in-process and is lost on restart.
- `add()` uses `putIfAbsent` — there is no update/overwrite API (no `PUT` endpoint yet).
- No authentication, authorization, or rate-limiting.
- No pagination or listing of keys (no `GET /keyvalue` to list keys).
- Expiry resolution is in minutes as provided by the API; TTL calculated internally in milliseconds.
- No background eviction thread: expiry is enforced on access only.
- Responses are basic and not standardized to an error DTO format.

## Roadmap / Next steps
- Add `PUT /keyvalue` to allow upsert/overwrite semantics.
- Add `GET /keyvalue` listing (with pagination) and key filtering.
- Add background eviction or scheduled cleanup for expired keys.
- Add persistence option (Redis or file-based) behind a repository interface.
- Add integration tests and API contract tests.
- Add OpenAPI/Swagger documentation and structured error responses.
- Add authentication (JWT/Basic) and role-based permissions for write/delete operations.

## Contact / Notes
This is an initial educational/demo implementation. Use it for development or testing only. For production needs, add persistence, security, and operational features.

