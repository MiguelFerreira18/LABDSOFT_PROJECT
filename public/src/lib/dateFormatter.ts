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

  return months[monthNumber - 1] || '';
}

function formatDate(date: string | Date): string {
  let d: Date;

  if (Array.isArray(date)) {
    const [year, month, day, hours = 0, minutes = 0] = date;
    d = new Date(year, month - 1, day, hours, minutes);
  } else {
    d = new Date(date);
  }

  if (isNaN(d.getTime())) {
    return 'Invalid date';
  }

  const day = d.getDate().toString().padStart(2, '0'); // Ensure 2-digit day
  const month = getMonthName(d.getMonth() + 1); // Month name (1-indexed)
  const year = d.getFullYear(); // Full year

  return `${day} ${month} ${year}`; // Format as "10 August 2026"
}

export { formatDate }; // Exporta a função para ser reutilizada em outros arquivos
