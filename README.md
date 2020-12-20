# spen-and-chapin
Лабораторная работа по МСиСвИнфТ. БГУИР, 2020
## Использование
```Java
ChapinMetrics chapinMetrics = new ChapinMetrics(rawMethodCode); // init metrics with raw text
SpenMetrics spenMetrics = new SpenMetrics(rawMethodCode);

// use this to get metrics counts themselves
spenMetrics.getSpens(); chapinMetrics.getChapinTypes(); chapinMetrics.getIOChapinTypes()
```
