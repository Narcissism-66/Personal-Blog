import { ref } from 'vue';

const socket = ref(null);
const reconnectInterval = ref(null);
const reconnectAttempts = ref(0);
const maxReconnectAttempts = 10; // 最大重连尝试次数
const reconnectDelay = 1000; // 重连延迟时间（毫秒）

export function useWebSocket(url) {
    if (!socket.value) {
        console.log(`Attempting to connect to WebSocket server at ${url}`);
        socket.value = new WebSocket(url);

        socket.value.onopen = () => {
            console.log('WebSocket connection established');
            reconnectAttempts.value = 0; // 重置重连尝试次数
            clearInterval(reconnectInterval.value); // 清除重连定时器
        };

        socket.value.onclose = () => {
            console.log('WebSocket connection closed');
            if (reconnectAttempts.value < maxReconnectAttempts) {
                reconnectAttempts.value++;
                reconnectInterval.value = setTimeout(() => {
                    console.log('Attempting to reconnect to WebSocket server');
                    useWebSocket(url);
                }, reconnectDelay);
            } else {
                console.error('Max reconnect attempts reached. Giving up.');
            }
        };

        socket.value.onerror = (error) => {
            console.error('WebSocket error', error);
        };
    }

    return {
        socket,
    };
}

export function sendMessage(message) {
    if (socket.value && socket.value.readyState === WebSocket.OPEN) {
        console.log(`Sending message: ${JSON.stringify(message)}`);
        socket.value.send(JSON.stringify(message));
    } else {
        console.error('WebSocket is not open');
        // 如果连接未打开，可以在这里添加一个重试机制
        // setTimeout(() => sendMessage(message), 500); // 500ms 后重试
    }
}

export function closeWebSocket() {
    if (socket.value) {
        socket.value.close();
        socket.value = null;
        clearInterval(reconnectInterval.value); // 清除重连定时器
    }
}