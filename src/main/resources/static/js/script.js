    function sort(m_id,option){
        let c = $('#ctg').val();
        let t = $('#type').val();
        let from1 = $('#from').val();
        let till1 = $('#till').val();

            $.get("/sortOut",{
               mfc_id: m_id,
               sort_type: option,
               category : c,
               type : t,
               from:from1,
               till:till1
           },function (result){
                if(result!="") {
                   let div = $('#sortOut');
                   div.html("");

                   let parsed = JSON.parse(result);


                   for (let i = 0; i < parsed.length; i++) {
                       let img = parsed[i].img;
                       let item_id = parsed[i].id;
                       let name = parsed[i].name;
                       let details = parsed[i].details;
                       let price = parsed[i].price;

                       div.append(
                           ' <div class="col">\n' +
                           '     <div class="card h-100">\n' +
                           '          <div  class="img__div p-3">\n' +
                           '              <img src="' + img + '" class="card-img-top" alt="product">\n' +
                           '           </div>\n' +
                           '           <div class="card-body p-3">\n' +
                           '               <a class="link-warning" href="/details/' + item_id + '">\n' +
                           '                  <h5 class="card-title">' + name + '</h5>\n' +
                           '               </a>\n' +
                           '               <p  class="card-text text-muted">' + details + '</p>\n' +
                           '           </div>\n' +
                           '           <div  style="background-color: white; padding-bottom: 15px ;text-align: center">\n' +
                           '               <p class="fw-bold fs-3">' + price + ' $</p>                                       ' +
                           '               <a onclick="basket(' + item_id + ')"  class="btn btn-outline-primary btn-lg">add to basket</a>\n' +
                           '           </div>\n' +
                           '     </div>\n' +
                           '</div>'
                       );
                   }
               }else{
                  document.getElementById('modal-trigger').click();
                }
           });
        }

    function basket(id){
        $.get("/addToBasket/"+id);
    }

    function minus(id1){
       let n = document.getElementById(id1);
       let t = document.getElementById('total-price');
       let amount1 = parseInt(n.innerText);
       if(amount1>0){
           amount1--;
           n.innerText = amount1;
           $.get('/amount',{
               id: id1,
               amount: amount1
           },function (result){
               t.innerText = parseFloat(result)+'  $';
           });
       }
    }

    function plus(id1){
        let n = document.getElementById(id1);
        let t = document.getElementById('total-price');
        let amount1 = parseInt(n.innerText);
        amount1++;
        n.innerText = amount1;
        $.get('/amount',{
            id: id1,
            amount:amount1
        },function (result){
            t.innerText = parseFloat(result)+'  $';
        });
    }
