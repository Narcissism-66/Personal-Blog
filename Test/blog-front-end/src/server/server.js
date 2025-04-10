import express from 'express';
import http from 'http';
import { WebSocketServer } from 'ws';

// 创建一个 Express 应用
const app = express();

// 创建一个 HTTP 服务器
const server = http.createServer(app);

// 将 HTTP 服务器绑定到 WebSocket 服务器
const wss = new WebSocketServer({ server });

// WebSocket 连接事件
wss.on('connection', (socket) => {
    console.log('New client connected');

    socket.on('message', (message) => {
        console.log(`Received message: ${message}`);
        try {
            const data = JSON.parse(message);
            if (data.type === 'onlineUpdate') {
                wss.clients.forEach((client) => {
                    if (client.readyState === WebSocketServer.OPEN) {
                        client.send(JSON.stringify({
                            type: 'onlineUpdate',
                            onlineCount: data.onlineCount
                        }));
                    }
                });
            } else {
                console.error('Invalid message type:', data.type);
                socket.send(JSON.stringify({
                    type: 'error',
                    message: 'Invalid message type'
                }));
            }
        } catch (error) {
            console.error('Error parsing message:', error);
            socket.send(JSON.stringify({
                type: 'error',
                message: 'Invalid message format'
            }));
        }
    });

    socket.on('close', () => {
        console.log('Client disconnected');
    });

    socket.on('error', (error) => {
        console.error('Socket error:', error);
    });
});

// 启动 HTTP 服务器
const PORT = process.env.PORT || 5173;
server.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});