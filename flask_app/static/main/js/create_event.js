document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('create-event-form');
    const message = document.getElementById('form-message');
  
    form.addEventListener('submit', function (e) {
      const startDate = new Date(form.start_date.value);
      const endDate = new Date(form.end_date.value);
      const startTime = form.start_time.value;
      const endTime = form.end_time.value;
  
      message.textContent = "";
  
      if (startDate > endDate) {
        e.preventDefault();
        message.textContent = "Start date cannot be after end date.";
        return;
      }
  
      if (startTime >= endTime) {
        e.preventDefault();
        message.textContent = "Start time must be before end time.";
        return;
      }
  
      const emails = form.invitees.value.split(',').map(e => e.trim());
      const invalidEmails = emails.filter(email => !validateEmail(email));
      if (invalidEmails.length > 0) {
        e.preventDefault();
        message.textContent = "Some emails are invalid: " + invalidEmails.join(', ');
      }
    });
  
    function validateEmail(email) {
      const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return re.test(email);
    }
  });
  