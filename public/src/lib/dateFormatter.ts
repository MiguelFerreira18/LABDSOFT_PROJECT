// Função para mapear o número do mês para a abreviação do mês
function getMonthName(monthNumber: number): string {
  const months = [
    'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec',
  ];

  return months[monthNumber - 1] || ''; // Retorna o mês correspondente, ou uma string vazia se inválido
}

// Função para formatar a data corretamente
function formatDate(date: any): string {
  // Caso a data seja um array como o retornado no JSON
  const d = new Date(date[0], date[1] - 1, date[2], date[3], date[4]); // Ajusta para mês 0-indexed
  if (isNaN(d.getTime())) {
    return 'Invalid date'; // Se não for uma data válida, retorna 'Invalid date'
  }

  const day = d.getDate().toString().padStart(2, '0'); // Obtém o dia com 2 dígitos
  const month = getMonthName(d.getMonth() + 1); // Obtém o mês com 1-indexed (padrão)
  const year = d.getFullYear(); // Obtém o ano

  return `${day} ${month} ${year}`; // Formata como "21 Feb 2024"
}

export { formatDate }; // Exporta a função para ser reutilizada em outros arquivos
