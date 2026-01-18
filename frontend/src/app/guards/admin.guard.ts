import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  
const router = inject(Router);
const role = localStorage.getItem("role");

if (role === "ADMIN") {
  return true;
} else {
  router.navigate(['/login']); // or unauthorized page
  return false;
}

};