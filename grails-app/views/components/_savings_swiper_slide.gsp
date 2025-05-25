
                            <div class="swiper-slide" onclick="window.location.href='${createLink(action: 'show', id:activeSavings.id)}';">
                                <div class="row justify-content-center">
                                    <div class="col-9 mx-3 text-white " style="background-image: linear-gradient(to bottom right, #4675F4, #0A42DA); border-radius: 20px;">
                                        <div class="row pt-4 justify-content-center">
                                            <div class="col-5">
                                                <div class="row">
                                                    <div class="col-12 fs-5 fw-light" style="font-family: 'Lato';font-weight: 100;font-style: light;">
                                                        Balance
                                                    </div>
                                                    <div class="col-12 fs-3" style="font-family: 'Lato';font-weight: 400;font-style: normal;">
                                                        &#x20B1;<g:formatNumber number="${activeSavings?.balance}" format="#,##0.00" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-5 text-end">
                                                <img src='${assetPath(src: "card_account_icon.svg")}' alt="card-icon">
                                            </div>
                                        </div>
                                        <div class="row py-3 justify-content-center">
                                            <div class="col-5">
                                                <div class="row ">
                                                    <div class="col-12 fs-5" style="font-family: 'Lato';font-weight: 100;font-style: light;">
                                                        Account Name
                                                    </div>
                                                    <div class="col-12 fs-5" style="font-family: 'Lato';font-weight: 400;font-style: normal;">
                                                        ${activeSavings?.acctName}
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-5">
                                                <div class="row">
                                                    <div class="col-12 fs-5" style="font-family: 'Lato';font-weight: 100;font-style: light;">
                                                        Valid Thru
                                                    </div>
                                                    <div class="col-12 fs-5" style="font-family: 'Lato';font-weight: 400;font-style: normal;">
                                                        <g:formatDate format="MM/yy" date="${activeSavings?.expiryDate}"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row py-3 justify-content-center">
                                            <div class="col-7 fs-2" style="font-family: 'Lato';font-weight: 400;font-style: normal;">
                                                ${activeSavings?.acctNo}
                                            </div>
                                            <div class="col-3 text-end">
                                                <img src='${assetPath(src: "card_account_icon1.svg")}' alt="card-icon">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>