// Função para mapear o número do mês para a abreviação do mês
function getMonthName(monthNumber: number): string {
    const months = [
      "Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];
  
    return months[monthNumber - 1] || '';  // Retorna o mês correspondente, ou uma string vazia se inválido
  }
  

  function formatDate(date: string | Date): string {
    const d = new Date(date);
    if (isNaN(d.getTime())) {
      // Se não for uma data válida, retorna uma string de fallback
      return "Invalid date";
    }
  
    const day = d.getDate().toString().padStart(2, '0');  // Obtém o dia e garante que tenha 2 dígitos
    const month = getMonthName(d.getMonth() + 1);  // Obtém o mês, lembrando que getMonth() retorna 0-11
    const year = d.getFullYear();  // Obtém o ano
  
    return `${day} ${month} ${year}`;  // Formata como "21 Feb 2024"
  }
  
  export { formatDate };  // Exporta a função para ser reutilizada em outros arquivos
  