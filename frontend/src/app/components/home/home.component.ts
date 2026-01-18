import { Component, ViewChild, ElementRef, AfterViewInit, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  @ViewChild('dealGrid', { static: false }) dealGrid!: ElementRef;
  products:Product[]=[]
  info:string="";
  contactForm: FormGroup;
  
  navigateToProductList() {
    this.router.navigate(['user/view-products']);
  }
  
constructor(private router: Router,private productService:ProductService,private userService:UserService,private fb: FormBuilder) {}



  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data)=>{
        this.products=data;
      },
      error: (error)=>{
        console.error("Error loading products")
      }
    })
    
    
this.contactForm = this.fb.group({
  name: ['', [Validators.required, Validators.minLength(2)]],
  mobile: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
  email: ['', [Validators.required, Validators.email]],
  support: ['', [Validators.required]],
  message: [''], // optional
});

  }


 

showToast = false;

onSubmit() {
  if(this.contactForm.valid){
    this.info=this.contactForm.get('message').value;
    this.userService.sendContactInfo(this.info).subscribe(d=>{
      console.log(d);
    })
  }


  }


goToproductList(){
  this.router.navigate(['/user/view-products'])
}

get name() { return this.contactForm.get('name'); }
get mobile() { return this.contactForm.get('mobile'); }
get email() { return this.contactForm.get('email'); }
get support() { return this.contactForm.get('support'); }
get message() { return this.contactForm.get('message'); }

}




