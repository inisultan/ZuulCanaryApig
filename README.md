###ZuulCanaryApig

Sample Code showing how to utilize Zuul Netflix framework to do canary test implementation in back end system

Utilizing key `FilterConstants.LOAD_BALANCER_KEY` on RequestContext and `choose` method on `IRule`

sample cURL 

```
curl --location --request GET 'http://localhost:8089/listall' --header 'X-Custom-Route: 8080'
```