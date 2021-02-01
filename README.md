# kaka-es-jd

结合es仿京东搜索

1、通过http://localhost:9090/parse/java ，执行完该请求之后就会向es里插入数据，其中java是keywords，可以替换成其他词，例如vue；
2、通过http://localhost:9090/，可以直接访问到页面，分页已经在index.html配置好了；


注意：当http://localhost:9090/parse/汉语，执行请求可以插入数据，但是搜索的时候，由于采用的默认分词器，所以直接查询汉语是查不出来的，可以查汉或者语；
