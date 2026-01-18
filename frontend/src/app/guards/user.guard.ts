import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const userGuard: CanActivateFn = (route, state) => {
  
const router = inject(Router);
const role = localStorage.getItem("role");

if (role === "USER") {
  return true;
} else {
  router.navigate(['/login']); // or redirect to unauthorized page
  return false;
}
};
