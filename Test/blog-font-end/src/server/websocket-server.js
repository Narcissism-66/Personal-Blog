import { WebSocketServer } from 'ws';

const createWebSocketServer = (server) => {
    const wss = new WebSocketServer({ server });
    console.log('WebSocket server instance created');

    wss.on('connection', (socket) => {
        console.log('New client connected');

        socket.on('message', (message) => {
            console.log('Raw message:', message); // 记录原始消息内容
            try {
                const data = JSON.parse(message);
                console.log('Received message:', data);
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
            console.error('WebSocket error:', error);
        });
    });

    return wss;
};

export default createWebSocketServer;
