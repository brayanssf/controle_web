// API Base URL
const API_BASE = window.location.origin;

// ==================== UTILIDADES ====================

// Obter Token JWT
function obterToken() {
    return localStorage.getItem('token');
}

// Obter Dados do Usuário
function obterDadosUsuario() {
    const dados = localStorage.getItem('dadosUsuario');
    return dados ? JSON.parse(dados) : null;
}

// Verificar se é Admin
function ehAdmin() {
    const usuario = obterDadosUsuario();
    return usuario && usuario.papel === 'ADMIN';
}

// Logout
function sair() {
    localStorage.removeItem('token');
    localStorage.removeItem('dadosUsuario');
    window.location.href = '/login.html';
}

// ==================== REQUISIÇÕES API ====================

async function requisicaoAPI(endpoint, opcoes = {}) {
    const token = obterToken();
    const headers = {
        'Content-Type': 'application/json',
        ...opcoes.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            ...opcoes,
            headers
        });

        if (response.status === 401) {
            sair();
            throw new Error('Sessão expirada');
        }

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Erro na requisição');
        }

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return response.json();
        }

        return response;
    } catch (error) {
        console.error('Erro na requisição:', error);
        throw error;
    }
}

// ==================== UI HELPERS ====================

function mostrarAlerta(mensagem, tipo = 'success') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${tipo}`;
    alertDiv.textContent = mensagem;
    alertDiv.setAttribute('role', 'alert');

    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alertDiv, container.firstChild);

        setTimeout(() => alertDiv.remove(), 5000);
    }
}

function mostrarCarregando(elemento) {
    elemento.innerHTML = `
        <div class="loading">
            <div class="spinner"></div>
            <p>Carregando...</p>
        </div>
    `;
}

function formatarData(dataString) {
    const data = new Date(dataString);
    return data.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatarDataInput(dataString) {
    const data = new Date(dataString);
    const ano = data.getFullYear();
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const dia = String(data.getDate()).padStart(2, '0');
    const horas = String(data.getHours()).padStart(2, '0');
    const minutos = String(data.getMinutes()).padStart(2, '0');
    return `${ano}-${mes}-${dia}T${horas}:${minutos}`;
}

// ==================== AUTENTICAÇÃO ====================

function verificarAutenticacao() {
    const token = obterToken();
    const paginasPublicas = ['/login.html', '/cadastro.html'];
    const paginaAtual = window.location.pathname;

    if (!token && !paginasPublicas.some(pagina => paginaAtual.endsWith(pagina))) {
        window.location.href = '/login.html';
        return false;
    }

    if (token && paginasPublicas.some(pagina => paginaAtual.endsWith(pagina))) {
        window.location.href = '/eventos.html';
        return false;
    }

    return true;
}

function verificarAcessoAdmin() {
    if (!ehAdmin()) {
        mostrarAlerta('Acesso negado. Apenas administradores.', 'error');
        setTimeout(() => window.location.href = '/eventos.html', 2000);
        return false;
    }
    return true;
}

// ==================== NAVEGAÇÃO ====================

function atualizarNavegacao() {
    const usuario = obterDadosUsuario();
    if (!usuario) return;

    const nav = document.querySelector('nav');
    if (!nav) return;

    const linksAdmin = `
        <a href="/eventos.html">Eventos</a>
        <a href="/presencas.html">Presenças</a>
        <a href="/relatorios.html">Relatórios</a>
        <a href="#" onclick="sair()" class="btn btn-secondary">Sair</a>
    `;

    const linksUsuario = `
        <a href="/eventos.html">Eventos</a>
        <a href="#" onclick="sair()" class="btn btn-secondary">Sair</a>
    `;

    nav.innerHTML = ehAdmin() ? linksAdmin : linksUsuario;

    // Adicionar informações do usuário
    const userInfo = document.createElement('div');
    userInfo.className = 'user-info';
    userInfo.textContent = `Olá, ${usuario.nome}`;
    document.querySelector('header .container').appendChild(userInfo);

    // Marcar página ativa
    const paginaAtual = window.location.pathname;
    nav.querySelectorAll('a').forEach(link => {
        if (link.getAttribute('href') === paginaAtual) {
            link.classList.add('active');
        }
    });
}

// ==================== INICIALIZAÇÃO ====================

document.addEventListener('DOMContentLoaded', () => {
    if (verificarAutenticacao()) {
        atualizarNavegacao();
    }
});

// Expor funções globalmente
window.obterToken = obterToken;
window.obterDadosUsuario = obterDadosUsuario;
window.ehAdmin = ehAdmin;
window.sair = sair;
window.requisicaoAPI = requisicaoAPI;
window.mostrarAlerta = mostrarAlerta;
window.mostrarCarregando = mostrarCarregando;
window.formatarData = formatarData;
window.formatarDataInput = formatarDataInput;
window.verificarAcessoAdmin = verificarAcessoAdmin;