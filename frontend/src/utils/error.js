/**
 * Handles error responses from the backend API
 * @param {Error} error - The error object from axios
 * @returns {string} - A user-friendly error message
 */
export const handleApiError = (error) => {
  if (!error.response) {
    return 'Network error. Please check your connection and try again.';
  }

  const { data } = error.response;

  // Handle validation errors
  if (data.validationErrors && Array.isArray(data.validationErrors)) {
    return data.validationErrors.map(err => err.message).join('\n');
  }

  // Handle specific error codes
  switch (data.errorCode) {
    case 'EMAIL_EXISTS':
      return 'This email is already registered.';
    case 'INVALID_CREDENTIALS':
      return 'Invalid email or password.';
    case 'VALIDATION_ERROR':
      return data.message || 'Please check your input and try again.';
    case 'INTERNAL_SERVER_ERROR':
      return 'An unexpected error occurred. Please try again later.';
    default:
      return data.message || 'Something went wrong. Please try again.';
  }
}; 