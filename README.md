*OOP_Project*
It's OOP project of K.Aliev, T.Ossepyan, D.Cherepkow

## Here are demo data sample which is seeded on empty start
feel free to use
P.S.: we do not reccomend to update this values to avoid confusion

### Seeded Users
| ID | Name | Email | Password | Role |
|----|------|-------|----------|------|
| A1 | AdminName | admin@uni.kz | admin123 | Admin |
| M1 | ManagerName | manager@uni.kz | manager123 | Manager (OR) |
| P1 | Pakita | pakita@uni.kz | pakita123 | Teacher (Professor) |
| L1 | Beken | beken@uni.kz | beken123 | Teacher (Lecturer) |
| S1 | Fedya | fedya@uni.kz | fedya123 | Student (Year 1) |
| S2 | Dima | dima@uni.kz | dima123 | Student (Year 2) |
| S3 | Karim | karim@uni.kz | karim123 | Student (Year 3) |
| S4 | Tevos | tevos@uni.kz | tevos123 | Student (Year 4) |
| R1 | Roberto | roberto@uni.kz | roberto123 | ResearchEmployee |
| R2 | Alice | alice@uni.kz | alice123 | ResearchEmployee |
| D1 | DeanName | dean@uni.kz | dean123 | Dean |
| E1 | RectorName | rector@uni.kz | rector123 | Rector |

### Seeded Courses
| Course ID | Course Name | Credits | Year |
|-----------|-------------|---------|------|
| CS103 | Web Development | 4 | 2 |
| CS101 | Intro to Computer Science | 10 | 1 |
| MA102 | Calculus I | 20 | 1 |
| CS201 | OOP | 3 | 2 |

## How to Run

From the project root:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out demo.Main
```

### Notes
- On first run, data is seeded automatically if `data.ser` is missing or empty.
- On exit (`0` in menu), current state is saved into `data.ser`.




