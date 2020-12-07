eureka-server服务相当于是所有微服务的管理中心。  
third-part-index-data-project相当于是第三方的数据服务中心，这里由于第三方数据服务都不是免费的，所以模拟了一下  
index-gather-store-service用于将数据从第三方取出来并保存至缓存中
index-data-service用于从缓存中读取具体某一家公司的数据  
index-codes-service用于读取公司的信息  
index-zuul-service是一个网关微服务，用于*统一入口*，*鉴权校验*，*动态路由*，*降低服务端和客户端耦合*