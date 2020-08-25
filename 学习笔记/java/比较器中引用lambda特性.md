```
list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getStoreCd()))), ArrayList::new));
```

去熟练

