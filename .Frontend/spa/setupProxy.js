/*const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://localhost:8080', // Адрес вашего Spring Boot сервера
      changeOrigin: true,
      pathRewrite: {
        '^/api': '', // Перезапись пути, если необходимо
      },
    })
  );
}*/