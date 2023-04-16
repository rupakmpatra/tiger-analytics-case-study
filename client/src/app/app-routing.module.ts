import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PricingFeedComponent } from './pricing-feed/pricing-feed.component';

const routes: Routes = [{
  path: "pricing-feed", component: PricingFeedComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
