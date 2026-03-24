const API = '/api/contas';

document.addEventListener('DOMContentLoaded', () => {
    carregarContas();
    document.getElementById('formConta').addEventListener('submit', salvar);
});

async function carregarContas() {
    try {
        const res = await fetch(API);
        if (!res.ok) throw new Error();
        renderizarTabela(await res.json());
    } catch {
        mostrarToast('Não foi possível carregar as contas', 'erro');
        renderizarTabela([]);
    }
}

async function buscarContas() {
    const termo = document.getElementById('busca').value.trim();
    const url = termo ? `${API}?nome=${encodeURIComponent(termo)}` : API;
    try {
        const res = await fetch(url);
        if (!res.ok) throw new Error();
        renderizarTabela(await res.json());
    } catch {
        mostrarToast('Erro ao buscar contas', 'erro');
    }
}

async function salvar(e) {
    e.preventDefault();

    const nome  = document.getElementById('nome').value.trim();
    const saldo = document.getElementById('saldo').value;
    if (!validarForm(nome, saldo)) return;

    const id     = document.getElementById('contaId').value;
    const url    = id ? `${API}/${id}` : API;
    const metodo = id ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method: metodo,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, saldo: parseFloat(saldo) })
        });
        const data = await res.json();

        if (!res.ok) {
            mostrarToast(data.erro || 'Erro ao salvar conta', 'erro');
            return;
        }

        mostrarToast(id ? 'Conta atualizada!' : 'Conta criada com sucesso!', 'sucesso');
        limparForm();
        carregarContas();
    } catch {
        mostrarToast('Erro de conexão com o servidor', 'erro');
    }
}

function editarConta(id, nome, saldo) {
    document.getElementById('contaId').value = id;
    document.getElementById('nome').value    = nome;
    document.getElementById('saldo').value   = saldo;
    document.getElementById('form-titulo').textContent = 'Editar Conta';
    document.getElementById('btnCancelar').style.display = 'inline-block';
    document.getElementById('nome').focus();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

let idParaExcluir = null;

function confirmarExcluir(id, nome) {
    idParaExcluir = id;
    document.getElementById('textoModal').textContent =
        `Tem certeza que deseja excluir a conta de "${nome}"?`;
    document.getElementById('modalExcluir').style.display = 'flex';
    document.getElementById('btnConfirmarExcluir').onclick = async () => {
        fecharModal();
        await excluirConta(idParaExcluir);
    };
}

async function excluirConta(id) {
    try {
        const res = await fetch(`${API}/${id}`, { method: 'DELETE' });
        if (res.ok) {
            mostrarToast('Conta excluída', 'info');
            carregarContas();
        } else {
            const data = await res.json();
            mostrarToast(data.erro || 'Erro ao excluir', 'erro');
        }
    } catch {
        mostrarToast('Erro de conexão com o servidor', 'erro');
    }
}

function fecharModal() {
    document.getElementById('modalExcluir').style.display = 'none';
    idParaExcluir = null;
}

function renderizarTabela(contas) {
    const corpo = document.getElementById('corpoTabela');
    if (!contas.length) {
        corpo.innerHTML = '<tr><td colspan="4" class="vazio">Nenhuma conta encontrada</td></tr>';
        return;
    }
    corpo.innerHTML = contas.map(c => `
        <tr>
            <td>${c.id}</td>
            <td>${escapeHtml(c.nome)}</td>
            <td>R$ ${c.saldo.toFixed(2)}</td>
            <td>
                <button class="btn-acao btn-editar"
                    onclick="editarConta(${c.id}, '${escapeHtml(c.nome)}', ${c.saldo})">
                    ✏️ Editar
                </button>
                <button class="btn-acao btn-perigo"
                    onclick="confirmarExcluir(${c.id}, '${escapeHtml(c.nome)}')">
                    🗑️ Excluir
                </button>
            </td>
        </tr>
    `).join('');
}

function validarForm(nome, saldo) {
    let valido = true;
    const erroNome  = document.getElementById('erroNome');
    const erroSaldo = document.getElementById('erroSaldo');

    erroNome.textContent  = '';
    erroSaldo.textContent = '';
    document.getElementById('nome').classList.remove('invalido');
    document.getElementById('saldo').classList.remove('invalido');

    if (!nome || nome.length < 2) {
        erroNome.textContent = 'Nome deve ter pelo menos 2 caracteres';
        document.getElementById('nome').classList.add('invalido');
        valido = false;
    }
    if (saldo === '' || isNaN(parseFloat(saldo))) {
        erroSaldo.textContent = 'Informe um saldo válido';
        document.getElementById('saldo').classList.add('invalido');
        valido = false;
    } else if (parseFloat(saldo) < 0) {
        erroSaldo.textContent = 'Saldo não pode ser negativo';
        document.getElementById('saldo').classList.add('invalido');
        valido = false;
    }
    return valido;
}

function limparForm() {
    ['contaId','nome','saldo'].forEach(id => document.getElementById(id).value = '');
    ['erroNome','erroSaldo'].forEach(id => document.getElementById(id).textContent = '');
    ['nome','saldo'].forEach(id => document.getElementById(id).classList.remove('invalido'));
    document.getElementById('form-titulo').textContent = 'Nova Conta';
    document.getElementById('btnCancelar').style.display = 'none';
}

function mostrarToast(msg, tipo) {
    const toast = document.getElementById('toast');
    toast.textContent = msg;
    toast.className = `toast ${tipo}`;
    toast.style.display = 'block';
    setTimeout(() => { toast.style.display = 'none'; }, 3500);
}

function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;').replace(/</g, '&lt;')
        .replace(/>/g, '&gt;').replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}
