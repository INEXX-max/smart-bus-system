# Smart Bus & Stop Coordination System

A Java simulation of an intelligent public transportation system. Uses simulated IoT sensors and cameras to make real-time decisions about whether buses should stop or skip stations based on passenger capacity.

## How It Works

1. Sensors detect the number of passengers waiting at the bus stop
2. Camera system checks the capacity of the arriving bus
3. System checks the capacity of the next bus behind
4. **Decision algorithm:**
   - Front bus full + back bus has space → **Skip stop** (passengers wait for emptier bus)
   - Front bus has space → **Stop normally**
   - Both buses full → **Stop anyway** (no optimization possible)

## Classes

| Class | Description |
|---|---|
| `AkilliOtobusSistemi` | Main class with decision algorithm |
| `Otobus` | Bus object — plate, capacity, passenger count, current stop |
| `KameraSistemi` | Camera simulation — counts passengers inside bus |
| `AkilliDurak` | Smart stop — IoT sensor simulation for waiting passengers |

## Tech Stack

- Java
- OOP (Object-Oriented Programming)
- Algorithm & Decision Logic

## How to Run

```bash
javac AkilliOtobusSistemi.java
java AkilliOtobusSistemi
```

---

Built by [INEXX](https://github.com/INEXX-max) — İnönü University, Computer Science
