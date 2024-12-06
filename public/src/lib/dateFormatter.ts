// Função para mapear o número do mês para a abreviação do mês
function getMonthName(monthNumber: number): string {
  const months = [
    'Jan',
    'Feb',
    'Mar',
    'Apr',
    'May',
    'Jun',
    'Jul',
    'Aug',
    'Sep',
    'Oct',
    'Nov',
    'Dec',
  ];

  return months[monthNumber - 1] || ''; // Retorna o mês correspondente, ou uma string vazia se inválido
}

function formatDate(date: string | Date): string {
  let d: Date;

  // Handle array input
  if (Array.isArray(date)) {
    const [year, month, day, hours = 0, minutes = 0] = date; // Destructure with defaults
    d = new Date(year, month - 1, day, hours, minutes); // Month is 0-indexed
  } else {
    // Fallback to standard Date parsing
    d = new Date(date);
  }

  if (isNaN(d.getTime())) {
    // If the date is invalid, return a fallback
    return 'Invalid date';
  }

  const day = d.getDate().toString().padStart(2, '0'); // Ensure 2-digit day
  const month = getMonthName(d.getMonth() + 1); // Month name (1-indexed)
  const year = d.getFullYear(); // Full year

  return `${day} ${month} ${year}`; // Format as "10 August 2026"
}

export { formatDate }; // Exporta a função para ser reutilizada em outros arquivos
