import { inject, Inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const loginGuard: CanActivateFn = (route, state) => {
  const router: Router = inject(Router);
  const token = localStorage.getItem('jwtToken');
  const role = localStorage.getItem('role');

  if (token && role) {
    if (role === 'ADMIN') {
      router.navigate(['/admin/view-products']);
      return false;
    } else if (role === 'USER') {
      router.navigate(['/']);
      return false;
    } else {
      return true;
    }
  } else {
    return true;
  }
};
