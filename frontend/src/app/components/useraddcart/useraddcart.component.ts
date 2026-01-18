import { Component, OnInit } from '@angular/core';
import { CartItem } from 'src/app/models/cart-item.model';
import { Cart } from 'src/app/models/cart.model';

@Component({
  selector: 'app-useraddcart',
  templateUrl: './useraddcart.component.html',
  styleUrls: ['./useraddcart.component.css']
})
export class CartComponent implements OnInit {
  cart: Cart = {
    cartId: 1,
    totalAmount: 0,
    quantity: 0,
    items: [
      {
        id: 1,
        product: {
          productId: 101,
          name: 'Sample Product',
          description: 'This is a sample product description.',
          price: 499,
          photoImage: 'https://via.placeholder.com/120',
          stock: 10
        },
        quantity: 1
      },
      {
        id: 2,
        product: {
          productId: 102,
          name: 'Another Product',
          description: 'Another product description.',
          price: 799,
          photoImage: 'https://via.placeholder.com/120',
          stock: 5
        },
        quantity: 2
      }
    ]
  };

  constructor() {}

  ngOnInit(): void {
    this.calculateTotal();
  }

  increaseQty(item: CartItem) {
    // if (item.quantity! < (item.product?.stock || 0)) {
    //   item.quantity!++;
    //   this.calculateTotal();
    // }
  }

  decreaseQty(item: CartItem) {
    // if (item.quantity! > 1) {
    //   item.quantity!--;
    //   this.calculateTotal();
    // }
  }

  removeItem(item: CartItem) {
    this.cart.items = this.cart.items?.filter(i => i.id !== item.id);
    this.calculateTotal();
  }

  placeOrder(item: CartItem) {
    alert(`Order placed for ${item.product?.name} (Qty: ${item.quantity})`);
  }

  calculateTotal() {
    // this.cart.totalAmount = this.cart.items?.reduce((sum, i) => sum + (i.product?.price || 0) * (i.quantity || 0), 0);
    // this.cart.quantity = this.cart.items?.reduce((sum, i) => sum + (i.quantity || 0), 0);
  }

}
